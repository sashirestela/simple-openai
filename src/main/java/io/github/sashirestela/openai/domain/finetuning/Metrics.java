package io.github.sashirestela.openai.domain.finetuning;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Metrics {

    private Double step;
    private Double trainLoss;
    private Double trainMeanTokenAccuracy;
    private Double validLoss;
    private Double validMeanTokenAccuracy;
    private Double fullValidLoss;
    private Double fullValidMeanTokenAccuracy;

}
