package com.ichi0915.Endpoint.Auto.Mapping.controller;

import com.ichi0915.Endpoint.Auto.Mapping.data.task.Task;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Implementations of this interface should perform orchestration of operations in a workflow. Often
 * will be used in conjunction with {@link AtomicOperation} instances.
 */
public interface OrchestrationProcessor {
	/**
	 * This is the invocation point of orchestration.
	 *
	 * @param key a unique key, used to de-dupe orchestration requests
	 * @return a list of results
	 */
	Task process(
		@Nullable String cloudProvider,
		@Nonnull List<AtomicOperation> atomicOperations,
		@Nonnull String key);

	@Deprecated
	default Task process(@Nonnull List<AtomicOperation> atomicOperations, @Nonnull String key) {
		return process(null, atomicOperations, key);
	}
}
