package com.ichi0915.Endpoint.Auto.Mapping.cache;

import com.netflix.spinnaker.kork.annotations.Beta;
import java.util.Collection;
import java.util.Map;

/**
 * CacheData is stored in a Cache. Attributes are facts about the CacheData that can be updated by
 * CachingAgents. Relationships are links to other CacheData.
 *
 * <p>Note: Not all caches may support a per record ttl
 */
@Beta
public interface CacheData {
    String getId();

    /** @return The ttl (in seconds) for this CacheData */
    int getTtlSeconds();

    Map<String, Object> getAttributes();

    /**
     * @return relationships for this CacheData, keyed by type returning a collection of ids for that
     *     type
     */
    Map<String, Collection<String>> getRelationships();
}
