package io.github.sashirestela.openai.domain.completion;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
public class LogProbs {

    @JsonProperty("text_offset")
    private List<Integer> textOffset;

    @JsonProperty("token_logprobs")
    private List<Double> tokenLogprobs;

    private List<String> tokens;

    @JsonProperty("top_logprobs")
    private List<List<Double>> topLogprobs;

}