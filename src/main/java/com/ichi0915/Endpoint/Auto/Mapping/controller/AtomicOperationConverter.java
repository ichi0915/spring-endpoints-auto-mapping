package com.ichi0915.Endpoint.Auto.Mapping.controller;

import com.ichi0915.Endpoint.Auto.Mapping.description.OperationDescription;
import com.netflix.spinnaker.kork.annotations.Beta;
import java.util.Map;
import javax.annotation.Nullable;

/**
 * Implementations of this trait will provide an object capable of converting a Map of input
 * parameters to an operation's description object and an {@link AtomicOperation} instance.
 */
@Beta
public interface AtomicOperationConverter extends VersionedCloudProviderOperation {
	/**
	 * This method takes a Map input and converts it to an {@link AtomicOperation} instance.
	 *
	 * @param input
	 * @return atomic operation
	 */
	@Nullable
	AtomicOperation convertOperation(Map<String, Object> input);

	/**
	 * This method takes a Map input and creates a description object, that will often be used by an
	 * {@link AtomicOperation}.
	 *
	 * @param input
	 * @return instance of an operation description object
	 */
	OperationDescription convertDescription(Map<String, Object> input);
}