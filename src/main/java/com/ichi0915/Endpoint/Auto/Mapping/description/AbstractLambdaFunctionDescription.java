package com.ichi0915.Endpoint.Auto.Mapping.description;

import com.ichi0915.Endpoint.Auto.Mapping.resources.ApplicationNameable;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Data;

@Data
@EqualsAndHashCode(callSuper = false)
public abstract class AbstractLambdaFunctionDescription extends AbstractAmazonCredentialsDescription
		implements ApplicationNameable {
	String region;
	String appName;

	@Override
	public Collection<String> getApplications() {
		if (appName == null || appName.isEmpty()) {
			return Collections.emptyList();
		}
		return List.of(getAppName());
	}
}
