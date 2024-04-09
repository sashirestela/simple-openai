package io.github.sashirestela.openai.domain.image;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Size {

    @JsonProperty("256x256")
    X256,

    @JsonProperty("512x512")
    X512,

    @JsonProperty("1024x1024")
    X1024,

    @JsonProperty("1792x1024")
    X1792X,

    @JsonProperty("1024x1792")
    X1024X;

}
