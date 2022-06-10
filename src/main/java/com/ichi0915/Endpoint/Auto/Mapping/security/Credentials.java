package com.ichi0915.Endpoint.Auto.Mapping.security;

/** Credentials to an external system. Each credentials has a unique name for its type. */
public interface Credentials {

	/**
	 * Each credentials is uniquely identified by its name for a given type.
	 *
	 * @return Credentials name
	 */
	String getName();

	/** @return Credentials type */
	String getType();
}

