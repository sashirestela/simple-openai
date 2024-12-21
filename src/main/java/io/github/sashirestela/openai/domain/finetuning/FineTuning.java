package io.github.sashirestela.openai.domain.finetuning;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@NoArgsConstructor
@Getter
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FineTuning {

    private String id;
    private Long createdAt;
    private FineTuningError error;
    private String fineTunedModel;
    private Long finishedAt;
    private HyperParams hyperparameters;
    private String model;
    private String object;
    private String organizationId;
    private List<String> resultFiles;
    private String status;
    private Integer trainedTokens;
    private String trainingFile;
    private String validationFile;
    private List<Integration> integrations;
    private Integer seed;
    private Integer estimatedFinish;
    private MethodFineTunning method;

    @NoArgsConstructor
    @Getter
    @ToString
    public static class FineTuningError {

        private String code;
        private String message;
        private String param;

    }

}
