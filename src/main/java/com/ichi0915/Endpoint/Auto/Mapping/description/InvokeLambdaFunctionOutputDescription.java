package com.ichi0915.Endpoint.Auto.Mapping.description;

import com.amazonaws.services.lambda.model.InvokeResult;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class InvokeLambdaFunctionOutputDescription {
	InvokeResult invokeResult;
	private String responseString;
}