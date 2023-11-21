package io.github.sashirestela.openai.domain.completion;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
public class Choice {

    private String text;

    private Integer index;

    private LogProbs logprobs;

    @JsonProperty("finish_reason")
    private String finishReason;

}