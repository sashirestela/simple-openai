package io.github.sashirestela.openai.domain.assistant.v2;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.JsonNode;
import io.github.sashirestela.openai.function.FunctionDef;
import io.github.sashirestela.openai.support.JsonSchemaUtil;
import io.github.sashirestela.slimvalidator.constraints.Required;
import io.github.sashirestela.slimvalidator.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AssistantTool {

    public static final AssistantTool CODE_INTERPRETER = new AssistantTool(ToolType.CODE_INTERPRETER, null);
    public static final AssistantTool FILE_SEARCH = new AssistantTool(ToolType.FILE_SEARCH, null);

    private ToolType type;
    private AssistantFunctionDef function;

    public static AssistantTool function(FunctionDef function) {
        return new AssistantTool(ToolType.FUNCTION, new AssistantFunctionDef(
                function.getName(),
                function.getDescription(),
                JsonSchemaUtil.classToJsonSchema(function.getFunctionalClass())));
    }

    public static List<AssistantTool> functions(List<FunctionDef> functions) {
        return functions.stream()
                .map(funDef -> function(funDef))
                .collect(Collectors.toList());
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @ToString
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class AssistantFunctionDef {

        @Required
        @Size(max = 64)
        private String name;

        @JsonInclude(Include.NON_NULL)
        private String description;

        @Required
        JsonNode parameters;

    }

}
