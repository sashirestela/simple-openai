package io.github.sashirestela.openai.domain.chat.tool;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ChatToolChoiceType {

    @JsonProperty("none")
    NONE,

    @JsonProperty("auto")
    AUTO;

}
