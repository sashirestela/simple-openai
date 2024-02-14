package io.github.sashirestela.openai.domain.finetuning;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FineTuningResponse {

    private String id;
    private String object;
    private Long createdAt;
    private Long finishedAt;
    private String model;
    private String fineTunedModel;
    private String organizationId;
    private String status;
    private HyperParams hyperparameters;
    private String trainingFile;
    private String validationFile;
    private List<String> resultFiles;
    private Integer trainedTokens;
    private FineTuningError error;

}