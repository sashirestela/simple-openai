package io.github.sashirestela.openai.domain.image;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ImageRespFmt {

    @JsonProperty("url")
    URL,

    @JsonProperty("b64_json")
    B64JSON;

}