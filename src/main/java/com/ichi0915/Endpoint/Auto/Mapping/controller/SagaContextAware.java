package com.ichi0915.Endpoint.Auto.Mapping.controller;

import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import lombok.Data;

/**
 * Used to bridge AtomicOperations with Sagas.
 *
 * <p>Unfortunately, AtomicOperations and their descriptions are pretty well decoupled from their
 * original input. This makes it difficult to retry operations without re-sending the entire
 * operation ayload.
 */
public interface SagaContextAware {
	void setSagaContext(@Nonnull SagaContext sagaContext);

	@Nullable
	SagaContext getSagaContext();

	@Data
	class SagaContext {
		private String cloudProvider;
		private String descriptionName;
		private Map originalInput;
		private String sagaId;

		public SagaContext(String cloudProvider, String descriptionName, Map originalInput) {
			this.cloudProvider = cloudProvider;
			this.descriptionName = descriptionName;
			this.originalInput = originalInput;
		}
	}
}
