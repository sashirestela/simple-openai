package io.github.sashirestela.openai.common.audio;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum InputAudioFormat {

    @JsonProperty("wav")
    WAV,

    @JsonProperty("mp3")
    MP3;

}
