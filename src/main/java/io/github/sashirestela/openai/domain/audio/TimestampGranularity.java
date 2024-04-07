package io.github.sashirestela.openai.domain.audio;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum TimestampGranularity {

    @JsonProperty("word")
    WORD,

    @JsonProperty("segment")
    SEGMENT;

}
