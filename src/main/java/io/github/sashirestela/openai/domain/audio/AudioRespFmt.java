package io.github.sashirestela.openai.domain.audio;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum AudioRespFmt {

    @JsonProperty("json")
    JSON,

    @JsonProperty("text")
    TEXT,

    @JsonProperty("srt")
    SRT,

    @JsonProperty("verbose_json")
    VERBOSE_JSON,

    @JsonProperty("vtt")
    VTT;

}
