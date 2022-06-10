package com.ichi0915.Endpoint.Auto.Mapping.description;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ichi0915.Endpoint.Auto.Mapping.resources.CredentialsNameable;
import com.ichi0915.Endpoint.Auto.Mapping.security.NetflixAmazonCredentials;

abstract class AbstractAmazonCredentialsDescription implements CredentialsNameable {
	@JsonIgnore
	NetflixAmazonCredentials credentials;
	String account;

	@JsonProperty
	@Override
	public String getAccount() {
		return credentials.getName() != null ? credentials.getName() : account;
		// return credentials?.name ?: account;
	}
}