package com.ichi0915.Endpoint.Auto.Mapping.controller;

import com.ichi0915.Endpoint.Auto.Mapping.description.OperationDescription;
import com.ichi0915.Endpoint.Auto.Mapping.exceptions.AtomicOperationNotFoundException;
import com.ichi0915.Endpoint.Auto.Mapping.exceptions.DescriptionValidationException;
import com.netflix.spinnaker.kork.web.exceptions.ExceptionMessageDecorator;
import com.netflix.spinnaker.kork.exceptions.SystemException;
import com.ichi0915.Endpoint.Auto.Mapping.security.AuthenticatedRequest;
import com.netflix.spectator.api.Id;
import com.netflix.spectator.api.Registry;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.core.ResolvableType;
import org.springframework.lang.Nullable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Splitter;

import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OperationsService {

	private final Splitter COMMA_SPLITTER = Splitter.on(",");
	private final List<AtomicOperationDescriptionPreProcessor>atomicOperationDescriptionPreProcessors;
	private final Collection<AllowedAccountsValidator> allowedAccountValidators;
	private final ExceptionMessageDecorator exceptionMessageDecorator;
	private final List<DescriptionAuthorizer> descriptionAuthorizers;
	private final AtomicOperationsRegistry atomicOperationsRegistry;
	private final ObjectMapper objectMapper;
	private final Registry registry;
	private final Id validationErrorsCounterId;

	public OperationsService(ExceptionMessageDecorator exceptionMessageDecorator,
				AtomicOperationsRegistry atomicOperationsRegistry, ObjectMapper objectMapper,
				List<DescriptionAuthorizer> descriptionAuthorizers, Registry registry,
				Optional<Collection<AllowedAccountsValidator>> allowedAccountValidators,
				Optional<List<AtomicOperationDescriptionPreProcessor>>atomicOperationDescriptionPreProcessors) {
		this.exceptionMessageDecorator = exceptionMessageDecorator;
		this.objectMapper = objectMapper;
		this.descriptionAuthorizers = descriptionAuthorizers;
		this.registry = registry;
		this.atomicOperationsRegistry = atomicOperationsRegistry;
		this.allowedAccountValidators = allowedAccountValidators.orElse(Collections.emptyList());
		this.atomicOperationDescriptionPreProcessors =
			atomicOperationDescriptionPreProcessors.orElse(Collections.emptyList());
		validationErrorsCounterId = registry.createId("validationErrors");
	}

	@NonNull
	public List<AtomicOperation> collectAtomicOperations(
			@NonNull List<Map<String, Map<String, Object>>> inputs) {
		return collectAtomicOperations(null, inputs);
	}

	@NonNull
	public List<AtomicOperation> collectAtomicOperations(
		@Nullable String cloudProvider, @NonNull List<Map<String, Map<String, Object>>> inputs) {
		List<AtomicOperationBindingResult> results = convert(cloudProvider, inputs);

		List<AtomicOperation> atomicOperations = new ArrayList<>();
		results.forEach(
			bindingResult -> {
			if (bindingResult.errors.hasErrors()) {
				Collection<String> errors = collectErrors(bindingResult.errors);
				throw new DescriptionValidationException(errors);
			}
			atomicOperations.add(bindingResult.atomicOperation);
			});
		return atomicOperations;
	}

	private List<AtomicOperationBindingResult> convert(
		@Nullable String cloudProvider, @NonNull List<Map<String, Map<String, Object>>> inputs) {

		String username = AuthenticatedRequest.getSpinnakerUser().orElse("unknown");
		List<String> allowedAccounts =
			COMMA_SPLITTER.splitToList(AuthenticatedRequest.getSpinnakerAccounts().orElse(""));

		List<OperationDescription> descriptions = new ArrayList<>();
		return inputs.stream()
			.flatMap(
				input ->
					input.entrySet().stream()
						.map(
							e -> {
							final String descriptionName = e.getKey();
							final Map<String, Object> descriptionInput = e.getValue();
							final OperationInput operationInput =
								objectMapper.convertValue(descriptionInput, OperationInput.class);
							final String provider =
								Optional.ofNullable(cloudProvider)
									.orElse(operationInput.cloudProvider);

							AtomicOperationConverter converter =
								atomicOperationsRegistry.getAtomicOperationConverter(
									descriptionName, provider);

							// TODO(rz): What if a preprocessor fails due to a downstream error? How
							// does this affect retrying?
							Map<String, Object> processedInput =
								processDescriptionInput(
									atomicOperationDescriptionPreProcessors,
									converter,
									descriptionInput);

							OperationDescription description =
								converter.convertDescription(processedInput);

							descriptions.add(description);

							DescriptionValidationErrors errors =
								new DescriptionValidationErrors(description);

							DescriptionValidator validator =
								atomicOperationsRegistry.getAtomicOperationDescriptionValidator(
									DescriptionValidator.getValidatorName(descriptionName), provider);

							if (validator == null) {
								String operationName =
									Optional.ofNullable(description)
										.map(it -> it.getClass().getSimpleName())
										.orElse("UNKNOWN");
								log.warn(
									"No validator found for operation {} and cloud provider {}",
									operationName,
									provider);
							} else {
								// TODO(rz): Assert description is T
								validator.validate(descriptions, description, errors);
							}

//							allowedAccountValidators.forEach(
//								it -> it.validate(username, allowedAccounts, description, errors));

//							if (description != null) {
//								DescriptionAuthorizer descriptionAuthorizer =
//									descriptionAuthorizers.stream()
//										.filter(it -> it.supports(description))
//										.findFirst()
//										.orElseThrow(
//											() ->
//												new SystemException(
//													"Unable to find supporting description authorizer for {}",
//													description.getClass().getSimpleName()));
//
//								descriptionAuthorizer.authorize(description, errors);
//							}

							// TODO(rz): This is so bad. We convert the description input twice (once
							// above) and then once inside of this convertOperation procedure. This
							// means that we do a bunch of serde work twice without needing to.
							AtomicOperation atomicOperation =
								converter.convertOperation(processedInput);
							if (atomicOperation == null) {
								throw new AtomicOperationNotFoundException(descriptionName);
							}

							if (atomicOperation instanceof SagaContextAware) {
								((SagaContextAware) atomicOperation)
									.setSagaContext(
										new SagaContextAware.SagaContext(
											cloudProvider, descriptionName, descriptionInput));
							}

							if (errors.hasErrors()) {
								registry
									.counter(
										validationErrorsCounterId.withTag(
											"operation", atomicOperation.getClass().getSimpleName()))
									.increment();
							}

							return new AtomicOperationBindingResult(atomicOperation, errors);
							}))
			.collect(Collectors.toList());
	}

	/**
	 * Process the validation {@link Errors} and transform errors to a collection of strings so they
	 * can be added to the exception.
	 */
	private Collection<String> collectErrors(Errors errors) {
		Collection<String> errorCollection = new ArrayList<>();
		for (ObjectError objectError : errors.getAllErrors()) {
		if (objectError.getDefaultMessage() != null && objectError.getCode() != null) {
			errorCollection.add(
				exceptionMessageDecorator.decorate(
					objectError.getCode(), objectError.getDefaultMessage()));
		} else if (objectError.getCode() != null) {
			// Treat the error code as the default message - better than nothing I guess.
			errorCollection.add(
				exceptionMessageDecorator.decorate(objectError.getCode(), objectError.getCode()));
		}
		}
		return errorCollection;
	}

	/**
	 * Runs the provided descriptionInput through preprocessors.
	 *
	 * <p>Which preprocessors are used is determined by doing some reflection on the
	 * AtomicOperationConverter's return type.
	 */
	private static Map<String, Object> processDescriptionInput(
		Collection<AtomicOperationDescriptionPreProcessor> descriptionPreProcessors,
		AtomicOperationConverter converter,
		Map<String, Object> descriptionInput) {

		Method convertDescriptionMethod;
		try {
			convertDescriptionMethod = converter.getClass().getMethod("convertDescription", Map.class);
		}
		catch (NoSuchMethodException e) {
			throw new SystemException("Could not find convertDescription method on converter", e);
		}

		Class<?> convertDescriptionReturnType =
			ResolvableType.forMethodReturnType(convertDescriptionMethod).getRawClass();

		for (AtomicOperationDescriptionPreProcessor preProcessor : descriptionPreProcessors) {
			if (preProcessor.supports(convertDescriptionReturnType)) {
				descriptionInput = preProcessor.process(descriptionInput);
			}
		}

		return descriptionInput;
	}

	@Value
	public static class AtomicOperationBindingResult {
		private AtomicOperation atomicOperation;
		private Errors errors;
	}

	@Data
	private static class OperationInput {
		@Nullable private String credentials;
		@Nullable private String accountName;
		@Nullable private String account;
		@Nullable private String cloudProvider;

		@Nullable
		public String computeAccountName() {
			return Optional.ofNullable(credentials)
				.orElse(Optional.ofNullable(accountName).orElse(account));
		}
	}

	// @Data
	// @AllArgsConstructor
	// private static class SagaAndSnapshot {
	// 	Saga saga;
	// 	SnapshotAtomicOperationInputCommand snapshot;
	// }
}
