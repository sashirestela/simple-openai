package io.github.sashirestela.openai.domain.audio;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
@Builder
public class AudioSpeechRequest {

    @NonNull
    private String model;

    @NonNull
    private String input;

    @NonNull
    private Voice voice;

    @JsonInclude(Include.NON_NULL)
    @JsonProperty("response_format")
    private SpeechRespFmt responseFormat;

    @JsonInclude(Include.NON_NULL)
    private Double speed;
}