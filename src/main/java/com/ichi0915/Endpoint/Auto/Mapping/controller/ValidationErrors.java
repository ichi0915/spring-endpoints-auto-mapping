package com.ichi0915.Endpoint.Auto.Mapping.controller;

import com.netflix.spinnaker.kork.annotations.Beta;

@Beta
public interface ValidationErrors {

	void reject(String errorCode);

	void reject(String errorCode, String defaultMessage);

	void reject(String errorCode, Object[] errorArgs, String defaultMessage);

	void rejectValue(String field, String errorCode);

	void rejectValue(String field, String errorCode, String defaultMessage);

	void rejectValue(String field, String errorCode, Object[] errorArgs, String defaultMessage);
}