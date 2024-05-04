package io.github.sashirestela.openai.tool;

import io.github.sashirestela.openai.function.FunctionCall;
import io.github.sashirestela.slimvalidator.constraints.Required;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class ToolCall {

    private Integer index;

    @Required
    private String id;

    @Required
    private ToolType type;

    @Required
    private FunctionCall function;

}
