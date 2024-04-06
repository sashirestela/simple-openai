package io.github.sashirestela.openai.domain.assistant;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.sashirestela.slimvalidator.constraints.Required;
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
public class AssistantTool {

    public static final AssistantTool CODE_INTERPRETER = AssistantTool.builder()
            .type("code_interpreter")
            .build();
    public static final AssistantTool RETRIEVAL = AssistantTool.builder()
            .type("retrieval")
            .build();

    @Required
    @Builder.Default
    private String type = "function";

    private AssistantFunction function;

}
