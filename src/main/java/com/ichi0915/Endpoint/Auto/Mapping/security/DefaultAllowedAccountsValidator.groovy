package com.ichi0915.Endpoint.Auto.Mapping.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ichi0915.Endpoint.Auto.Mapping.resources.NonCredentialed;
import com.netflix.spinnaker.fiat.model.Authorization;
import com.netflix.spinnaker.fiat.shared.FiatStatus
import groovy.util.logging.Slf4j;
//import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;

import java.util.Collection;
import java.util.List;

@Slf4j
public class DefaultAllowedAccountsValidator implements AllowedAccountsValidator {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final AccountCredentialsProvider accountCredentialsProvider;
    private final FiatStatus fiatStatus;

    public DefaultAllowedAccountsValidator(AccountCredentialsProvider accountCredentialsProvider, FiatStatus fiatStatus) {
        this.accountCredentialsProvider = accountCredentialsProvider;
        this.fiatStatus = fiatStatus;
    }

    @Override
    void validate(String user, Collection<String> allowedAccounts, Object description, Errors errors) {
        if (fiatStatus.isEnabled()) {
            // fiat has it's own mechanisms for verifying access to an account
            return ;
        }

        if (!accountCredentialsProvider.all.find {
            it.requiredGroupMembership || ((it instanceof AbstractAccountCredentials) && it.permissions?.isRestricted())
        }) {
            // no accounts have group restrictions so no need to validate / log
            return ;
        }

        /*
         * Access should be allowed iff
         * - the account is not restricted (has no requiredGroupMembership)
         * - the user has been granted specific access (has the target account in its set of allowed accounts)
         */
        if (description.hasProperty("credentials")) {
            if (description.credentials instanceof Collection) {
                description.credentials.each { AccountCredentials credentials ->
                    validateTargetAccount(credentials, allowedAccounts, description, user, errors);
                }
            } else {
                validateTargetAccount(description.credentials, allowedAccounts, description, user, errors);
            }
        } else {
            if (!(description instanceof NonCredentialed)) {
                errors.rejectValue("credentials", "missing", "no credentials found in description: ${description.class.simpleName})");
            }
        }
    }

    private void validateTargetAccount(AccountCredentials credentials, Collection<String> allowedAccounts, Object description, String user, Errors errors) {
        List<String> requiredGroups = [];
        boolean anonymousAllowed = true;
        if ((credentials instanceof AbstractAccountCredentials) && credentials.permissions?.isRestricted()) {
            anonymousAllowed = false;
            if (credentials.requiredGroupMembership) {
                log.warn("For account ${credentials.name}: using permissions ${credentials.permissions} over ${credentials.requiredGroupMembership} for authorization check.");
            }
            requiredGroups = credentials.permissions.get(Authorization.WRITE).collect { it.toLowerCase() };
        } else if (credentials.requiredGroupMembership) {
            anonymousAllowed = false;
            requiredGroups = credentials.requiredGroupMembership*.toLowerCase();
        }
        String targetAccount = credentials.name;
        Boolean isAuthorized = anonymousAllowed || allowedAccounts.find { it.equalsIgnoreCase(targetAccount) };-
        def json = null;
        try {
            json = OBJECT_MAPPER.writeValueAsString(description);
        } catch (Exception ignored) {
        }
        String message = "${user} is ${isAuthorized ? '' : 'not '}authorized (account: ${targetAccount}, description: ${description.class.simpleName}, allowedAccounts: ${allowedAccounts}, requiredGroups: ${requiredGroups}, json: ${json})";
        if (!isAuthorized) {
            log.warn(message);
            errors.rejectValue("credentials", "unauthorized", "${user} is not authorized (account: ${targetAccount}, description: ${description.class.simpleName})");
        } else {
            log.info(message);
        }
    }
}
