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
public class Checkpoint {

    private String id;
    private Integer createdAt;
    private String fineTunedModelCheckpoint;
    private Integer stepNumber;
    private Metrics metrics;
    private String fineTuningJobId;
    private String object;

}
