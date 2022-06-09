package com.ichi0915.Endpoint.Auto.Mapping.controller;

import com.netflix.spinnaker.kork.annotations.Beta;
import java.util.List;

@Beta
public abstract class DescriptionValidator<T> implements VersionedCloudProviderOperation {
	private static final String VALIDATOR_SUFFIX = "Validator";

	public static String getValidatorName(String description) {
		return description + VALIDATOR_SUFFIX;
	}

	public static String getOperationName(String validator) {
		if (validator == null || validator.length() == 0) {
			return validator;
		}
		if (validator.endsWith(VALIDATOR_SUFFIX)) {
			return validator.substring(0, validator.length() - VALIDATOR_SUFFIX.length());
		}
		return validator;
	}

	public abstract void validate(List<T> priorDescriptions, T description, ValidationErrors errors);
}
