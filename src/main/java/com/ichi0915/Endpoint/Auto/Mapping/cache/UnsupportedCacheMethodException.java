package com.ichi0915.Endpoint.Auto.Mapping.cache;

import com.netflix.spinnaker.kork.annotations.Beta;

@Beta
public class UnsupportedCacheMethodException extends RuntimeException {
    public UnsupportedCacheMethodException(String message) {
        super(message);
    }
}
