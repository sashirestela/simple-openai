package io.github.sashirestela.openai.common.content;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ImageDetail {

    @JsonProperty("auto")
    AUTO,

    @JsonProperty("low")
    LOW,

    @JsonProperty("high")
    HIGH;

}
