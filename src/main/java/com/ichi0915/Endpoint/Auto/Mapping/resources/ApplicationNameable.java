package com.ichi0915.Endpoint.Auto.Mapping.resources;

import com.ichi0915.Endpoint.Auto.Mapping.description.OperationDescription;
//import com.netflix.spinnaker.clouddriver.orchestration.OperationDescription;

import java.util.Collection;


/** Denotes an operation description operates on one or more specific application resources. */
public interface ApplicationNameable extends OperationDescription {
	Collection<String> getApplications();
}
