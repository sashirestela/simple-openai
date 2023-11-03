package io.github.sashirestela.openai.domain.finetuning;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@AllArgsConstructor
@Getter
@Builder
public class FineTuningRequest {

    @NonNull
    @JsonProperty("training_file")
    private String trainingFile;

    @JsonInclude(Include.NON_NULL)
    @JsonProperty("validation_file")
    private String validationFile;

    @NonNull
    @JsonInclude(Include.NON_NULL)
    private String model;

    @JsonInclude(Include.NON_NULL)
    @JsonProperty("hyperparameters")
    private HyperParams hyperParameters;

    @JsonInclude(Include.NON_NULL)
    private String suffix;

}