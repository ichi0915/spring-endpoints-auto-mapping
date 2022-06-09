package com.ichi0915.Endpoint.Auto.Mapping.controller;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

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
