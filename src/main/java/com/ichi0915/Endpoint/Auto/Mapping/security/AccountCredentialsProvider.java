package com.ichi0915.Endpoint.Auto.Mapping.security;

import com.netflix.spinnaker.kork.annotations.Beta;
import java.util.Set;

/**
 * Implementations of this interface will provide a mechanism to store and retrieve {@link
 * AccountCredentials} objects. For manipulating the backing of this provider, consumers of this API
 * should get access to its corresponding {@link AccountCredentialsRepository}
 */
@Beta
public interface AccountCredentialsProvider {

    /**
     * Returns all of the accounts known to the repository of this provider.
     *
     * @return a set of account names
     */
    Set<? extends AccountCredentials> getAll();

    /**
     * Returns a specific {@link AccountCredentials} object a specified name
     *
     * @param name the name of the account
     * @return account credentials object
     */
    AccountCredentials getCredentials(String name);
}
