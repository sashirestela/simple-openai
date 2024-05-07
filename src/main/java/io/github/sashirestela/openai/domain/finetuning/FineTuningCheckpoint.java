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
public class FineTuningCheckpoint {

    private String id;
    private Integer createdAt;
    private String fineTunedModelCheckpoint;
    private Integer stepNumber;
    private Metrics metrics;
    private String fineTuningJobId;
    private String object;

    @NoArgsConstructor
    @Getter
    @ToString
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Metrics {

        private Double step;
        private Double trainLoss;
        private Double trainMeanTokenAccuracy;
        private Double validLoss;
        private Double validMeanTokenAccuracy;
        private Double fullValidLoss;
        private Double fullValidMeanTokenAccuracy;

    }

}
