package com.ichi0915.Endpoint.Auto.Mapping.provider;

import static com.ichi0915.Endpoint.Auto.Mapping.cache.Keys.Namespace.LAMBDA_FUNCTIONS;
import static com.netflix.spinnaker.clouddriver.core.provider.agent.Namespace.APPLICATIONS;
//import static com.netflix.spinnaker.clouddriver.lambda.cache.Keys.Namespace.LAMBDA_FUNCTIONS;

//import com.ichi0915.Endpoint.Auto.Mapping.cache.AwsDataKeys;
import com.amazonaws.services.lambda.model.FunctionConfiguration;
import com.ichi0915.Endpoint.Auto.Mapping.cache.*;
import com.netflix.spinnaker.clouddriver.model.Function;
import com.netflix.spinnaker.clouddriver.model.FunctionProvider;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LambdaFunctionProvider implements FunctionProvider {
    private LambdaCacheClient awsLambdaCacheClient;
    private final Cache cacheView;

    @Autowired
    public LambdaFunctionProvider(Cache cacheView) {
        this.awsLambdaCacheClient = new LambdaCacheClient(cacheView);
        this.cacheView = cacheView;
    }

    @Override
    public Collection<Function> getAllFunctions() {
        return new ArrayList<>(awsLambdaCacheClient.getAll());
    }

    public Function getFunction(String account, String region, String functionName) {
        String key = Keys.getLambdaFunctionKey(account, region, functionName);
        return awsLambdaCacheClient.get(key);
    }

    public Set<Function> getApplicationFunctions(String applicationName) {

        CacheData application =
                cacheView.get(
                        APPLICATIONS.ns,
                        //AwsDataKeys.getApplicationKey(applicationName));x
                        com.ichi0915.Endpoint.Auto.Mapping.cache.AwsDataKeys.getApplicationKey(applicationName));
                        //com.netflix.spinnaker.clouddriver.aws.data.Keys.getApplicationKey(applicationName));
                        //com.ichi0915.Endpoint.Auto.Mapping.cache.Keys.getApplicationKey(applicationName));

        Set<Function> appFunctions = new HashSet<>();
        if (null != application && null != application.getRelationships()) {
            Collection<String> functionRel = application.getRelationships().get(LAMBDA_FUNCTIONS.ns);
            if (null != functionRel && !functionRel.isEmpty()) {
                functionRel.forEach(
                        functionKey -> {
                            Function function = awsLambdaCacheClient.get(functionKey);
                            if (null != function) {
                                appFunctions.add(function);
                            }
                        });
            }
        } else {
            getAllFunctions().stream()
                    .filter(f -> f instanceof FunctionConfiguration)
                    .map(f -> (FunctionConfiguration) f)
                    .filter(f -> f.getFunctionName() != null)
                    .filter(f -> f.getFunctionName().startsWith(applicationName))
                    .forEach(f -> appFunctions.add((Function) f));
        }
        return appFunctions;
    }
}
