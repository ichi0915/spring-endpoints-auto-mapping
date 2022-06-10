package com.ichi0915.Endpoint.Auto.Mapping.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ichi0915.Endpoint.Auto.Mapping.security.AccountCredentials;

/**
 * Convenience trait for extracting the account name from a credential, which all descriptions
 * should have.
 */
public interface CredentialsNameable extends AccountNameable {
	AccountCredentials getCredentials();

	@JsonProperty("credentials")
	@Override
	default String getAccount() {
		return getCredentials().getName();
	}
}
