package com.ichi0915.Endpoint.Auto.Mapping.controller;

import com.amazonaws.services.lambda.model.PutFunctionConcurrencyResult;
import com.ichi0915.Endpoint.Auto.Mapping.description.InvokeLambdaFunctionDescription;
import com.ichi0915.Endpoint.Auto.Mapping.description.InvokeLambdaFunctionOutputDescription;
import com.ichi0915.Endpoint.Auto.Mapping.events.OperationEvent;

import java.util.Collection;
import java.util.List;

public class InvokeLambdaAtomicOperation
		extends AbstractLambdaAtomicOperation<
		InvokeLambdaFunctionDescription, InvokeLambdaFunctionOutputDescription>
		implements AtomicOperation<InvokeLambdaFunctionOutputDescription> {

	InvokeLambdaAtomicOperation(InvokeLambdaFunctionDescription description) {
		super(description, "INVOKE_LAMBDA_FUNCTION");
		//TODO Auto-generated constructor stub
	}

	@Override
	public InvokeLambdaFunctionOutputDescription operate(List priorOutputs) {
		System.out.println("entreeeee");
		return null;
	}

	// @Override
	// public Collection<OperationEvent> getEvents() {
	// 	return AtomicOperation.super.getEvents();
	// }
}