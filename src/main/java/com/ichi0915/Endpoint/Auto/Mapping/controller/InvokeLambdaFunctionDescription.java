package com.ichi0915.Endpoint.Auto.Mapping.controller;

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
}