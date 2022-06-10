package com.ichi0915.Endpoint.Auto.Mapping.security;

import java.util.Collection;
import java.util.List;

public interface AWSAccountInfoLookup {
	String findAccountId();

	List<AmazonCredentials.AWSRegion> listRegions(Collection<String> regionNames);

	List<AmazonCredentials.AWSRegion> listRegions(String... regionNames);

	List<String> listAvailabilityZones(String region);
}
