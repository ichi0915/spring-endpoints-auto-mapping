package com.ichi0915.Endpoint.Auto.Mapping.controller;

import com.amazonaws.services.lambda.AWSLambda;
import com.ichi0915.Endpoint.Auto.Mapping.description.AbstractLambdaFunctionDescription;
import com.ichi0915.Endpoint.Auto.Mapping.exceptions.InvalidAccountException;
import com.ichi0915.Endpoint.Auto.Mapping.provider.LambdaFunctionProvider;
import com.ichi0915.Endpoint.Auto.Mapping.security.AccountCredentialsProvider;
//import com.ichi0915.Endpoint.Auto.Mapping.security.AmazonClientProvider;
//import com.ichi0915.Endpoint.Auto.Mapping.security.NetflixAmazonCredentials;
import com.netflix.spinnaker.clouddriver.aws.security.AmazonClientProvider;
import com.netflix.spinnaker.clouddriver.aws.security.AmazonCredentials;
import com.netflix.spinnaker.clouddriver.aws.security.NetflixAmazonCredentials;
import com.netflix.spinnaker.clouddriver.data.task.Task;
import com.netflix.spinnaker.clouddriver.data.task.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractLambdaAtomicOperation<T extends AbstractLambdaFunctionDescription, K>
		implements AtomicOperation<K> {
	@Autowired
	AmazonClientProvider amazonClientProvider;

	@Autowired
	AccountCredentialsProvider accountCredentialsProvider;

	@Autowired
	LambdaFunctionProvider lambdaFunctionProvider;

	private final String basePhase;

	T description;

	AbstractLambdaAtomicOperation(T description, String basePhase) {
		this.description = description;
		this.basePhase = basePhase;
	}

	private static Task getTask() {
		return TaskRepository.threadLocalTask.get();
	}

	AWSLambda getLambdaClient() {
		String region = getRegion();
		NetflixAmazonCredentials credentialAccount = (NetflixAmazonCredentials) description.getCredentials();
		if (!credentialAccount.getLambdaEnabled()) {
			throw new InvalidAccountException("AWS Lambda is not enabled for provided account. \n");
		}
		return amazonClientProvider.getAmazonLambda(credentialAccount, region);
	}

	protected String getRegion() {
		return description.getRegion();
	}

	protected AmazonCredentials getCredentials() {
		return (AmazonCredentials) accountCredentialsProvider.getCredentials(description.getAccount());
	}

	void updateTaskStatus(String status) {
		getTask().updateStatus(basePhase, status);
	}
}