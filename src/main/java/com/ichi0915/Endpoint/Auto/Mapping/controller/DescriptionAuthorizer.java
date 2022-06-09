package com.ichi0915.Endpoint.Auto.Mapping.controller;

import org.springframework.validation.Errors;

/** Authorizes atomic operation description objects. */
public interface DescriptionAuthorizer {

	/** @param description The atomic operation description object this instance supports. */
	default boolean supports(Object description) {
		return true;
	}

	/** @param description - The atomic operation description object. */
	void authorize(OperationDescription description, Errors errors);
}
