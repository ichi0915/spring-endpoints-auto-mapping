package com.ichi0915.Endpoint.Auto.Mapping.controller;

import javax.annotation.Nullable;

/**
 * A registry which does a lookup of AtomicOperationConverters and DescriptionValidators based on their names and
 * cloud providers
 *
 */
interface AtomicOperationsRegistry {
	/**
	 *
	 * @param description
	 * @param cloudProvider
	 * @return
	 */
	AtomicOperationConverter getAtomicOperationConverter(String description, String cloudProvider);

	/**
	 *
	 * @param validator
	 * @param cloudProvider
	 * @return
	 */
	@Nullable DescriptionValidator getAtomicOperationDescriptionValidator(String validator, String cloudProvider);
}