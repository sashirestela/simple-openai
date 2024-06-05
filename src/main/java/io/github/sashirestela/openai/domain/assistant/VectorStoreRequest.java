package io.github.sashirestela.openai.domain.assistant;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.github.sashirestela.slimvalidator.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.util.List;
import java.util.Map;

@Getter
@Builder
@JsonInclude(Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class VectorStoreRequest {

    @Singular
    @Size(max = 500)
    private List<String> fileIds;

    private String name;

    private ExpiresAfter expiresAfter;

    private ChunkingStrategy chunkingStrategy;

    @Size(max = 16)
    private Map<String, String> metadata;

}
