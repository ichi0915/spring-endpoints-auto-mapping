package com.ichi0915.Endpoint.Auto.Mapping.data.task;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.NonNull;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(builderClassName = "SagaIdBuilder")
@JsonDeserialize(builder = SagaId.SagaIdBuilder.class)
public class SagaId {
	@NonNull String name;
	@NonNull String id;

	@JsonPOJOBuilder(withPrefix = "")
	public static class SagaIdBuilder {}
}
