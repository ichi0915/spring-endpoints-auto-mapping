package com.ichi0915.Endpoint.Auto.Mapping.controller;

import com.amazonaws.services.lambda.model.PutFunctionConcurrencyResult;
import com.ichi0915.Endpoint.Auto.Mapping.events.OperationEvent;

import java.util.Collection;
import java.util.List;

public class PutLambdaReservedConcurrencyAtomicOperation
        extends AbstractLambdaAtomicOperation<
        InvokeLambdaFunctionDescription, InvokeLambdaFunctionOutputDescription>
        implements AtomicOperation<InvokeLambdaFunctionOutputDescription> {


    @Override
    public PutFunctionConcurrencyResult operate(List<PutFunctionConcurrencyResult> priorOutputs) {
        System.out.println("entreeeee");
        return null;
    }

    @Override
    public Collection<OperationEvent> getEvents() {
        return AtomicOperation.super.getEvents();
    }
}