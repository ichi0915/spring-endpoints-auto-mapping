package com.ichi0915.Endpoint.Auto.Mapping.controller;

import com.netflix.spinnaker.kork.annotations.Beta;
import javax.annotation.Nullable;

@Beta
public interface VersionedCloudProviderOperation {
	/**
	 * Allows individual operations to be versioned.
	 *
	 * <p>A {@code version} may be null if no specific version is requested, which is the common case.
	 *
	 * @param version The operation version requested by the client
	 * @return true if the operation works with the provided version
	 */
	default boolean acceptsVersion(@Nullable String version) {
		return true;
	}
}