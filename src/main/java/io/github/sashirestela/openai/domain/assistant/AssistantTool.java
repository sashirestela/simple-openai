package io.github.sashirestela.openai.domain.assistant;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.github.sashirestela.openai.common.tool.Tool;
import io.github.sashirestela.openai.common.tool.ToolType;
import io.github.sashirestela.slimvalidator.constraints.Range;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AssistantTool extends Tool {

    private FileSearch fileSearch;

    private AssistantTool(ToolType type, FileSearch fileSearch) {
        super(type, null);
        this.fileSearch = fileSearch;
    }

    public static AssistantTool codeInterpreter() {
        return new AssistantTool(ToolType.CODE_INTERPRETER, null);
    }

    public static AssistantTool fileSearch() {
        return new AssistantTool(ToolType.FILE_SEARCH, null);
    }

    public static AssistantTool fileSearch(Integer maxNumResults) {
        return new AssistantTool(ToolType.FILE_SEARCH, new FileSearch(maxNumResults));
    }

    @NoArgsConstructor
    @Getter
    @ToString
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class FileSearch {

        @Range(min = 1, max = 50)
        private Integer maxNumResults;

        public FileSearch(Integer maxNumResults) {
            this.maxNumResults = maxNumResults;
        }

    }

}
