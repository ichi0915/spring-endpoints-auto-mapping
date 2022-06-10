package com.ichi0915.Endpoint.Auto.Mapping.config;

import com.ichi0915.Endpoint.Auto.Mapping.security.AccountCredentialsProvider;
import com.ichi0915.Endpoint.Auto.Mapping.security.AllowedAccountsValidator;
import com.ichi0915.Endpoint.Auto.Mapping.security.DefaultAllowedAccountsValidator;

import com.netflix.spinnaker.fiat.shared.EnableFiatAutoConfig;
import com.netflix.spinnaker.fiat.shared.FiatStatus;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableFiatAutoConfig
@EnableConfigurationProperties(OperationsSecurityConfigurationProperties)
class SecurityConfig {
	@Bean
	AllowedAccountsValidator allowedAccountsValidator(AccountCredentialsProvider accountCredentialsProvider,
													  FiatStatus fiatStatus) {
		return new DefaultAllowedAccountsValidator(accountCredentialsProvider, fiatStatus);
	}

	@ConfigurationProperties("operations.security")
	static class OperationsSecurityConfigurationProperties {
		SecurityAction onMissingSecuredCheck = SecurityAction.WARN;
		SecurityAction onMissingValidator = SecurityAction.WARN;

		//TODO(jonsie): should be `allowUnauthorizedImageTaggingInAccounts`
		List<String> allowUnauthenticatedImageTaggingInAccounts = new ArrayList<>();
	}

	static enum SecurityAction {
		IGNORE,
		WARN,
		FAIL
	}
}
