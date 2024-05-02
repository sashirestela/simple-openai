package io.github.sashirestela.openai.domain.assistant;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import io.github.sashirestela.openai.function.FunctionDef;
import io.github.sashirestela.openai.support.JsonSchemaUtil;
import io.github.sashirestela.slimvalidator.constraints.Required;
import io.github.sashirestela.slimvalidator.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AssistantFunction {

    private String description;

    @Required
    @Size(max = 64)
    private String name;

    @Required
    private JsonNode parameters;

    public static AssistantFunction function(FunctionDef function) {
        return AssistantFunction.builder()
                .name(function.getName())
                .description(function.getDescription())
                .parameters(JsonSchemaUtil.classToJsonSchema(function.getFunctionalClass()))
                .build();
    }

}
