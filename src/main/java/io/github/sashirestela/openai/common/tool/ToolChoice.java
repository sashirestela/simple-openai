package io.github.sashirestela.openai.common.tool;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.sashirestela.slimvalidator.constraints.Required;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ToolChoice {

    @Required
    private ToolType type;

    @Required
    private FunctionName function;

    public static ToolChoice function(String name) {
        return new ToolChoice(ToolType.FUNCTION, new FunctionName(name));
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @ToString
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class FunctionName {

        @Required
        private String name;

    }

}
