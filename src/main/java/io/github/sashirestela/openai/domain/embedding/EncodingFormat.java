package io.github.sashirestela.openai.domain.embedding;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum EncodingFormat {

    @JsonProperty("float")
    FLOAT,

    @JsonProperty("base64")
    BASE64;

}
