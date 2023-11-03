package io.github.sashirestela.openai.domain.finetuning;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
public class FineTuningResponse {

    private String id;

    private String object;

    @JsonProperty("created_at")
    private Long createdAt;

    @JsonProperty("finished_at")
    private Long finishedAt;

    private String model;

    @JsonProperty("fine_tuned_model")
    private String fineTunedModel;

    @JsonProperty("organization_id")
    private String organizationId;

    private String status;

    @JsonProperty("hyperparameters")
    private HyperParams hyperParameters;

    @JsonProperty("training_file")
    private String trainingFile;

    @JsonProperty("validation_file")
    private String validationFile;

    @JsonProperty("result_files")
    private List<String> resultFiles;

    @JsonProperty("trained_tokens")
    private Integer trainedTokens;

}