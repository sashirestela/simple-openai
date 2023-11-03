package io.github.sashirestela.openai.domain.audio;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class AudioTranscribeRequest extends AudioTranslateRequest {

    @JsonInclude(Include.NON_NULL)
    private String language;

}