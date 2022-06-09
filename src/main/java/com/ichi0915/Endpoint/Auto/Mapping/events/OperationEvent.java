package com.ichi0915.Endpoint.Auto.Mapping.events;

import com.netflix.spinnaker.kork.annotations.Beta;

@Beta
public interface OperationEvent {
	Type getType();

	Action getAction();

	String getCloudProvider();

	enum Type {
		SERVER_GROUP
	}

	enum Action {
		DELETE,
		CREATE
	}
}