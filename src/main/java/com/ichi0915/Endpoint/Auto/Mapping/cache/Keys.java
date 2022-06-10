package com.ichi0915.Endpoint.Auto.Mapping.cache;

//import static com.netflix.spinnaker.clouddriver.aws.AmazonCloudProvider.ID;

import com.google.common.base.CaseFormat;
import java.util.HashMap;
import java.util.Map;

public class Keys implements KeyParser {

    public static final String ID = "aws";

    public enum Namespace {
        IAM_ROLE,
        LAMBDA_FUNCTIONS;

        public final String ns;

        Namespace() {
            ns = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, this.name());
        }

        public String toString() {
            return ns;
        }
    }

    public static final String SEPARATOR = ":";

    @Override
    public String getCloudProvider() {
        return ID;
    }

    @Override
    public Map<String, String> parseKey(String key) {
        return parse(key);
    }

    @Override
    public Boolean canParseType(String type) {
        return canParse(type);
    }

    private static Boolean canParse(String type) {
        for (Namespace key : Namespace.values()) {
            if (key.toString().equals(type)) {
                return true;
            }
        }
        return false;
    }

    public static Map<String, String> parse(String key) {
        String[] parts = key.split(SEPARATOR);

        if (parts.length < 3 || !parts[0].equals(ID)) {
            return null;
        }

        Map<String, String> result = new HashMap<>();
        result.put("provider", parts[0]);
        result.put("type", parts[1]);
        result.put("account", parts[2]);

        Namespace namespace =
                Namespace.valueOf(CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, parts[1]));

        switch (namespace) {
            case LAMBDA_FUNCTIONS:
                result.put("region", parts[3]);
                result.put("AwsLambdaName", parts[4]);
                break;
            case IAM_ROLE:
                result.put("roleName", parts[3]);
                break;
            default:
                break;
        }

        return result;
    }

    @Override
    public Boolean canParseField(String type) {
        return false;
    }

    public static String getLambdaFunctionKey(String account, String region, String functionName) {
        return String.format(
                "%s:%s:%s:%s:%s", ID, Namespace.LAMBDA_FUNCTIONS, account, region, functionName);
    }

    public static String getIamRoleKey(String account, String iamRoleName) {
        return String.format("%s:%s:%s:%s", ID, Namespace.IAM_ROLE, account, iamRoleName);
    }
}
