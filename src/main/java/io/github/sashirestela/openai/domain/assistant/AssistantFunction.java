package io.github.sashirestela.openai.domain.assistant;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import io.github.sashirestela.openai.domain.chat.tool.ChatFunction;
import io.github.sashirestela.openai.support.JsonSchemaUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AssistantFunction {

    private String description;
    @NonNull
    private String name;
    @NonNull
    private JsonNode parameters;


    public static AssistantFunction function(ChatFunction function) {
        return AssistantFunction.builder()
                .name(function.getName())
                .description(function.getDescription())
                .parameters(JsonSchemaUtil.classToJsonSchema(function.getFunctionalClass()))
                .build();
    }
}