package io.github.sashirestela.openai.domain.assistant.v2;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.sashirestela.slimvalidator.constraints.Required;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ToolChoice {

    private ToolType type;
    private Object function;

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

    public enum ToolChoiceOption {

        @JsonProperty("none")
        NONE,

        @JsonProperty("auto")
        AUTO,

        @JsonProperty("required")
        REQUIRED;

    }

}
