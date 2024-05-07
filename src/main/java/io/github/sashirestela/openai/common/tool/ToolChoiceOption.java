package io.github.sashirestela.openai.common.tool;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ToolChoiceOption {

    @JsonProperty("none")
    NONE,

    @JsonProperty("auto")
    AUTO,

    @JsonProperty("required")
    REQUIRED;

}
