package io.github.sashirestela.openai.common.tool;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import io.github.sashirestela.openai.common.function.FunctionDef;
import io.github.sashirestela.openai.support.JsonSchemaUtil;
import io.github.sashirestela.slimvalidator.constraints.Required;
import io.github.sashirestela.slimvalidator.constraints.Size;
import lombok.AccessLevel;
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
public class Tool {

    protected ToolType type;
    protected ToolFunctionDef function;

    public static Tool function(FunctionDef function) {
        return new Tool(ToolType.FUNCTION,
                new ToolFunctionDef(
                        function.getName(),
                        function.getDescription(),
                        JsonSchemaUtil.classToJsonSchema(function.getFunctionalClass())));
    }

    public static List<Tool> functions(List<FunctionDef> functions) {
        return functions.stream()
                .map(funDef -> function(funDef))
                .collect(Collectors.toList());
    }

    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor
    @Getter
    @ToString
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ToolFunctionDef {

        @Required
        @Size(max = 64)
        private String name;

        private String description;

        @Required
        private JsonNode parameters;

    }

}
