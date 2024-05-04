package io.github.sashirestela.openai.domain.assistant;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ToolChoiceOption {

    @JsonProperty("none")
    NONE,

    @JsonProperty("auto")
    AUTO,

    @JsonProperty("required")
    REQUIRED;

}
