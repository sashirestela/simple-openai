package io.github.sashirestela.openai.domain.assistant;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.github.sashirestela.slimvalidator.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Map;

@Getter
@Builder
@JsonInclude(Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ToolResourceFull {

    private ToolResource.CodeInterpreter codeInterpreter;
    private FileSearch fileSearch;

    @Getter
    @SuperBuilder
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class FileSearch extends ToolResource.FileSearch {

        @Singular
        @Size(max = 1)
        private List<VectorStore> vectorStores;

        @Getter
        @Builder
        @JsonInclude(Include.NON_EMPTY)
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class VectorStore {

            @Singular
            @Size(max = 10_000)
            private List<String> fileIds;

            private ChunkingStrategy chunkingStrategy;

            @Size(max = 16)
            private Map<String, String> metadata;

        }

    }

}
