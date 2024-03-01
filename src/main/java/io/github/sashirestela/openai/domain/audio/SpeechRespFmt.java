package io.github.sashirestela.openai.domain.audio;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum SpeechRespFmt {

    @JsonProperty("mp3")
    MP3,

    @JsonProperty("opus")
    OPUS,

    @JsonProperty("aac")
    AAC,

    @JsonProperty("flac")
    FLAC;

}
