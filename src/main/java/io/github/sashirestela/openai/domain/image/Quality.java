package io.github.sashirestela.openai.domain.image;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Quality {

    // DALL-E-3 and DALL-E-2
    @JsonProperty("standard")
    STANDARD,

    // DALL-E-3
    @JsonProperty("hd")
    HD,

    // GPT-Image-1
    @JsonProperty("low")
    LOW,
    @JsonProperty("medium")
    MEDIUM,
    @JsonProperty("high")
    HIGH,

    // Default
    @JsonProperty("auto")
    AUTO;

}
