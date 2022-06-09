package com.ichi0915.Endpoint.Auto.Mapping.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TreeTraversingParser;
import java.io.IOException;
import java.util.Map;

/**
 * Provides an extension point for manipulating an {@code AtomicOperation} context prior to
 * execution.
 */
public interface AtomicOperationDescriptionPreProcessor {
	boolean supports(Class descriptionClass);

	Map<String, Object> process(Map<String, Object> description);

	default <T> T mapTo(ObjectMapper objectMapper, Map<String, Object> description, Class<T> clazz)
		throws IOException {
			ObjectNode objectNode = objectMapper.valueToTree(description);
			return objectMapper.readValue(new TreeTraversingParser(objectNode, objectMapper), clazz);
	}
}