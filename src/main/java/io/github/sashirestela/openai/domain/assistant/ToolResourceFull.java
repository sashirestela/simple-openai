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
public class ToolResourceFull {

    private CodeInterpreter codeInterpreter;
    private FileSearch fileSearch;

    @Getter
    @Builder
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class CodeInterpreter {

        @Singular
        @Size(max = 20)
        private List<String> fileIds;

    }

    @Getter
    @Builder
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class FileSearch {

        @Singular
        @Size(max = 1)
        private List<String> vectorStoreIds;

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

            @Size(max = 16)
            private Map<String, String> metadata;

        }

    }

}
