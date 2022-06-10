package com.ichi0915.Endpoint.Auto.Mapping.cache;

import com.netflix.spinnaker.kork.annotations.Beta;
import java.util.Collection;

@Beta
public interface CacheFilter {
    enum Type {
        RELATIONSHIP
    }

    Collection<String> filter(Type type, Collection<String> identifiers);
}
