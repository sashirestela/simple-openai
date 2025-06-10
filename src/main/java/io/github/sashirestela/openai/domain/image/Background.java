package io.github.sashirestela.openai.domain.image;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Background {

    @JsonProperty("transparent")
    TRANSPARENT,

    @JsonProperty("opaque")
    OPAQUE,

    @JsonProperty("auto")
    AUTO;

}
