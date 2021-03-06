package com.ichi0915.Endpoint.Auto.Mapping.cache;

import com.netflix.spinnaker.kork.annotations.Beta;
import java.util.Map;

@Beta
public interface KeyParser {

    /**
     * Returns the parsed property name for the specified <code>cache</code> that represents the
     * "name" of the item being parsed.
     *
     * <p>For example,
     *
     * <p>Given the AWS key <code>
     * serverGroups:mycluster-stack-detail:some-account:some-region:myservergroup-stack-detail-v000
     * </code>, we might store the server group name (the last part of the key) under a different
     * property than <code>name</code>, e.g., <code>serverGroup</code>, in which case the mapping of
     * Namespace.SERVER_GROUPS.ns to "serverGroup" would be needed.
     *
     * @param cache the name of the cache (key type) being parsed
     * @return the mapping of the key name to the actual key property name for the specified <code>
     *     cache</code> or <code>null</code> if no mapping exists or is required (e.g., if the parsed
     *     key already contains a <code>name</code> property and it maps correctly).
     */
    default String getNameMapping(String cache) {
        return null;
    }

    /**
     * Indicates which provider this particular parser handles
     *
     * @return the cloud provider ID
     */
    String getCloudProvider();

    /**
     * Parses the supplied key to an arbitrary Map of attributes
     *
     * @param key the full key
     * @return a Map of the key attributes
     */
    Map<String, String> parseKey(String key);

    /**
     * indicates whether this parser can parse the supplied type
     *
     * @param type the entity type, typically corresponding to a value in the implementing class's
     *     Namespace
     * @return <code>true</code> if it can parse this type, <code>false</code> otherwise
     */
    Boolean canParseType(String type);

    /**
     * indicates whether this parser can parse the supplied field
     *
     * @param field the entity type field, typically corresponding to a value in the implementing
     *     class's parsed Namespace field
     * @return <code>true</code> if it can parse this field, <code>false</code> otherwise
     */
    Boolean canParseField(String field);
}
