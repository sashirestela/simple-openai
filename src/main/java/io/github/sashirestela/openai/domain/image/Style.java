package io.github.sashirestela.openai.domain.image;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Style {

    @JsonProperty("vivid")
    VIVID,

    @JsonProperty("natural")
    NATURAL;
    
}