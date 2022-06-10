package com.ichi0915.Endpoint.Auto.Mapping.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ichi0915.Endpoint.Auto.Mapping.model.LambdaFunction;
import com.netflix.awsobjectmapper.AmazonObjectMapperConfigurer;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.ichi0915.Endpoint.Auto.Mapping.cache.Keys.Namespace.LAMBDA_FUNCTIONS;

@Component
public class LambdaCacheClient extends AbstractCacheClient<LambdaFunction> {
    private final ObjectMapper objectMapper = AmazonObjectMapperConfigurer.createConfigured();

    @Autowired
    public LambdaCacheClient(Cache cacheView) {
        super(cacheView, LAMBDA_FUNCTIONS.ns);
    }

    @Override
    protected LambdaFunction convert(CacheData cacheData) {
        Map<String, Object> attributes = cacheData.getAttributes();
        LambdaFunction lambdaFunction = objectMapper.convertValue(attributes, LambdaFunction.class);
        // Fix broken translation of uuid fields. Perhaps this is better fixed by configuring the
        // objectMapper right
        List<Map> eventSourceMappings = (List<Map>) attributes.get("eventSourceMappings");
        if (eventSourceMappings == null) {
            return lambdaFunction;
        }
        Map<String, String> arnUuidMap = new HashMap<>();
        eventSourceMappings.stream()
                .forEach(
                        xx -> {
                            arnUuidMap.put((String) xx.get("eventSourceArn"), (String) xx.get("uuid"));
                        });
        lambdaFunction
                .getEventSourceMappings()
                .forEach(
                        currMapping -> {
                            currMapping.setUUID((String) arnUuidMap.get(currMapping.getEventSourceArn()));
                        });
        return lambdaFunction;
    }
}
