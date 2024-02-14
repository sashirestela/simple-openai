package io.github.sashirestela.openai.domain.completion;

import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class LogProbs {

    private List<Integer> textOffset;
    private List<Double> tokenLogprobs;
    private List<String> tokens;
    private List<List<Double>> topLogprobs;

}