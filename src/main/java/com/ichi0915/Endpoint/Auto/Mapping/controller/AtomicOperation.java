package com.ichi0915.Endpoint.Auto.Mapping.controller;

import com.ichi0915.Endpoint.Auto.Mapping.events.OperationEvent;
import com.netflix.spinnaker.kork.annotations.Beta;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * An AtomicOperation is the most fundamental, low-level unit of work in a workflow. Implementations
 * of this interface should perform the simplest form of work possible, often described by a
 * description object.
 */
@Beta
public interface AtomicOperation<R> {
	/**
	 * This method will initiate the operation's work. In this, operation's can get a handle on prior
	 * output results from the required method argument.
	 *
	 * @param priorOutputs
	 * @return parameterized type
	 */
	R operate(List<R> priorOutputs);

	default Collection<OperationEvent> getEvents() {
		return Collections.emptyList();
	}
}
