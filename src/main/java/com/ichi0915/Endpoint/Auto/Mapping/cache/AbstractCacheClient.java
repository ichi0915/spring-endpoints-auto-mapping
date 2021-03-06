package com.ichi0915.Endpoint.Auto.Mapping.cache;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

abstract class AbstractCacheClient<T> {

    private final String keyNamespace;
    protected final Cache cacheView;

    /**
     * @param cacheView The Cache that the client will query.
     * @param keyNamespace The key namespace that the client is responsible for.
     */
    AbstractCacheClient(Cache cacheView, String keyNamespace) {
        this.cacheView = cacheView;
        this.keyNamespace = keyNamespace;
    }

    /**
     * @param cacheData CacheData that will be converted into an object.
     * @return An object of the generic type.
     */
    protected abstract T convert(CacheData cacheData);

    /** @return A list of all generic type objects belonging to the key namespace. */
    public Collection<T> getAll() {
        Collection<CacheData> allData = cacheView.getAll(keyNamespace);
        return convertAll(allData);
    }

    /**
     * @param account name of the AWS account, as defined in clouddriver.yml
     * @param region region of the AWS account, as defined in clouddriver.yml
     * @return A list of all generic type objects belonging to the account and region in the key
     *     namespace.
     */
    public Collection<T> getAll(String account, String region) {
        Collection<CacheData> data = fetchFromCache(account, region);
        return convertAll(data);
    }

    /**
     * @param key A key within the key namespace that will be used to retrieve the object.
     * @return An object of the generic type that is associated to the key.
     */
    public T get(String key) {
        CacheData cacheData = cacheView.get(keyNamespace, key);
        if (cacheData != null) {
            return convert(cacheData);
        }
        return null;
    }

    /**
     * @param cacheData A collection of CacheData that will be converted into a collection of generic
     *     typ objects.
     * @return A collection of generic typ objects.
     */
    private Collection<T> convertAll(Collection<CacheData> cacheData) {
        return cacheData.stream().map(this::convert).collect(Collectors.toList());
    }

    /**
     * @param account name of the AWS account, as defined in clouddriver.yml
     * @param region region of the AWS account, as defined in clouddriver.yml
     * @return
     */
    private Collection<CacheData> fetchFromCache(String account, String region) {
        String accountFilter = account != null ? account + Keys.SEPARATOR : "*" + Keys.SEPARATOR;
        String regionFilter = region != null ? region + Keys.SEPARATOR : "*" + Keys.SEPARATOR;
        Set<String> keys = new HashSet<>();
        String pattern =
                "aws" + Keys.SEPARATOR + keyNamespace + Keys.SEPARATOR + accountFilter + regionFilter + "*";
        Collection<String> nameMatches = cacheView.filterIdentifiers(keyNamespace, pattern);

        keys.addAll(nameMatches);

        Collection<CacheData> allData = cacheView.getAll(keyNamespace, keys);

        if (allData == null) {
            return Collections.emptyList();
        }

        return allData;
    }
}
