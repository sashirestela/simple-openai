package io.github.sashirestela.openai.domain.chat.content;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ImageDetail {

    @JsonProperty("auto")
    AUTO,

    @JsonProperty("low")
    LOW,

    @JsonProperty("high")
    HIGH;

}