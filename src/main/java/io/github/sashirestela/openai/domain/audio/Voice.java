package io.github.sashirestela.openai.domain.audio;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Voice {

    @JsonProperty("alloy")
    ALLOY,

    @JsonProperty("echo")
    ECHO,

    @JsonProperty("fable")
    FABLE,

    @JsonProperty("onyx")
    ONYX,

    @JsonProperty("nova")
    NOVA,

    @JsonProperty("shimmer")
    SHIMMER;

}
