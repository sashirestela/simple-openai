package io.github.sashirestela.openai.domain.image;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Quality {

    @JsonProperty("standard")
    STANDARD,

    @JsonProperty("hd")
    HD;

}
