package io.github.sashirestela.openai.tool;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ToolChoiceOption {

    @JsonProperty("none")
    NONE,

    @JsonProperty("auto")
    AUTO,

    @JsonProperty("required")
    REQUIRED;

}
