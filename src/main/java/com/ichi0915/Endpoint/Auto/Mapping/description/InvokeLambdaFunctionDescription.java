package com.ichi0915.Endpoint.Auto.Mapping.description;

import com.ichi0915.Endpoint.Auto.Mapping.security.AccountCredentials;
import com.netflix.spinnaker.kork.artifacts.model.Artifact;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class InvokeLambdaFunctionDescription extends AbstractLambdaFunctionDescription {
	String functionName;
	String qualifier;

	String payload;
	Artifact payloadArtifact;

	@Override
	public AccountCredentials getCredentials() {
		// TODO Auto-generated method stub
		return null;
	}
}