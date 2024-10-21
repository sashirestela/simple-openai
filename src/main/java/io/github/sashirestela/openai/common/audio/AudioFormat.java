package io.github.sashirestela.openai.common.audio;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum AudioFormat {

    @JsonProperty("mp3")
    MP3,

    @JsonProperty("opus")
    OPUS,

    @JsonProperty("aac")
    AAC,

    @JsonProperty("flac")
    FLAC,

    @JsonProperty("wav")
    WAV,

    @JsonProperty("pcm")
    PCM,

    @JsonProperty("pcm16")
    PCM16;

}
