package com.ichi0915.Endpoint.Auto.Mapping.model;

import com.amazonaws.services.lambda.model.AliasConfiguration;
import com.amazonaws.services.lambda.model.EventSourceMappingConfiguration;
import com.amazonaws.services.lambda.model.FunctionCodeLocation;
import com.amazonaws.services.lambda.model.FunctionConfiguration;
import com.netflix.spinnaker.clouddriver.model.Function;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)     //Warning
public class LambdaFunction extends FunctionConfiguration implements Function {
    private String cloudProvider;
    private String account;
    private String region;

    private Map<String, String> revisions;
    private List<AliasConfiguration> aliasConfigurations;
    private List<EventSourceMappingConfiguration> eventSourceMappings;
    private FunctionCodeLocation code;
    private Map<String, String> tags;
    private List<String> targetGroups;
}
