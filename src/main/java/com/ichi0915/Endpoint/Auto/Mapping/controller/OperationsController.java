package com.ichi0915.Endpoint.Auto.Mapping.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ichi0915.Endpoint.Auto.Mapping.data.task.Task;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class OperationsController {

	private final OperationsService operationsService;
	private final OrchestrationProcessor orchestrationProcessor;


	OperationsController(
		OperationsService operationsService,
		OrchestrationProcessor orchestrationProcessor,
		@Value("${admin.tasks.shutdown-wait-seconds:600}") long shutdownWaitSeconds) {
			this.operationsService = operationsService;
			this.orchestrationProcessor = orchestrationProcessor;
	}

	@PostMapping("/{cloudProvider}/ops/{name}")
	public StartOperationResult cloudProviderOperation(
		@PathVariable("cloudProvider") String cloudProvider,
		@PathVariable("name") String name,
		@RequestParam(value = "clientRequestId", required = false) String clientRequestId,
		@RequestBody Map requestBody) {

			// List<Map<String, Map<String, Object>>> inputs = new ArrayList<>(new HashMap<String, Map>(name, requestBody));
			List<Map<String, Map<String, Object>>> inputs = new ArrayList<>();
			Map<String, Map<String, Object>> data = new HashMap<String, Map<String, Object>>();
			data.put(name, requestBody);
			inputs.add(data);

			// List<AtomicOperation> atomicOperations = operationsService.collectAtomicOperations(cloudProvider, [[(name): requestBody]]);
			List<AtomicOperation> atomicOperations = operationsService.collectAtomicOperations(cloudProvider, inputs);
			return start(cloudProvider, atomicOperations, clientRequestId);
	}

	private StartOperationResult start(@Nullable String cloudProvider,
		@NonNull List<AtomicOperation> atomicOperations,
		@Nullable String id) {
			Task task = orchestrationProcessor.process(
				cloudProvider, atomicOperations, Optional.ofNullable(id).orElse(UUID.randomUUID().toString()));
			return new StartOperationResult(task.getId());
	}

	static class StartOperationResult {
		@JsonProperty private final String id;

		StartOperationResult(String id) {
			this.id = id;
		}

		@JsonProperty
		String getResourceUri() {
			return String.format("/task/%s", id);
		}
	}
}
