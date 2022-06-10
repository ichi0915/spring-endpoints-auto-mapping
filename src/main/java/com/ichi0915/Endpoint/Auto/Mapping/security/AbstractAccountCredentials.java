package com.ichi0915.Endpoint.Auto.Mapping.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.netflix.spinnaker.fiat.model.Authorization;
import com.ichi0915.Endpoint.Auto.Mapping.resources.Permissions;
//import com.netflix.spinnaker.fiat.model.resources.Permissions;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

// Todo: remove this class once these methods no longer need to be separated from AccountCredentials
public abstract class AbstractAccountCredentials<T> implements AccountCredentials<T> {

	// Todo: use jackson mixin on AccountCredentials rather than putting annotation here
	@JsonIgnore
	public abstract T getCredentials();

	// Todo: make Fiat an acceptable dependency for clouddriver-api and push up to AccountCredentials
	public Permissions getPermissions() {
		Set<String> rgm =
			Optional.ofNullable(getRequiredGroupMembership())
				.map(
					l ->
						l.stream()
							.map(
								s ->
									Optional.ofNullable(s)
										.map(String::trim)
										.map(String::toLowerCase)
										.orElse(""))
							.filter(s -> !s.isEmpty())
							.collect(Collectors.toSet()))
				.orElse(Collections.EMPTY_SET);
		if (rgm.isEmpty()) {
		return Permissions.EMPTY;
		}

		Permissions.Builder perms = new Permissions.Builder();
		for (String role : rgm) {
		perms.add(Authorization.READ, role);
		perms.add(Authorization.WRITE, role);
		}
		return perms.build();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		AccountCredentials that = (AccountCredentials) o;
		return Objects.equals(getName(), that.getName()) && Objects.equals(getType(), that.getType());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getName(), getType());
	}
}
