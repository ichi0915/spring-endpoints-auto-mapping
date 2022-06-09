package com.ichi0915.Endpoint.Auto.Mapping;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(value = {"com.netflix.spinnaker.kork.web.exceptions.ExceptionMessageDecorator"
		, "com.ichi0915.Endpoint.Auto.Mapping.api"})
@SpringBootApplication
public class EndpointAutoMappingApplication {

	public static void main(String[] args) {
		SpringApplication.run(EndpointAutoMappingApplication.class, args);
	}

}
