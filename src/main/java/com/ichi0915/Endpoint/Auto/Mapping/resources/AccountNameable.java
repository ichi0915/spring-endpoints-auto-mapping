package com.ichi0915.Endpoint.Auto.Mapping.resources;

import com.netflix.spinnaker.clouddriver.security.config.SecurityConfig;
//import com.ichi0915.Endpoint.Auto.Mapping.config.SecurityConfig;
import com.ichi0915.Endpoint.Auto.Mapping.description.OperationDescription;

public interface AccountNameable extends OperationDescription {
	String getAccount();

	/**
	 * @return whether or not this operation description expects to be further restricted by one or
	 *     more applications
	 */
	default boolean requiresApplicationRestriction() {
		return true;
	}

	default boolean requiresAuthorization(
		SecurityConfig.OperationsSecurityConfigurationProperties opsSecurityConfigProps) {
			return true;
	}
}
