package io.github.sashirestela.openai.domain.finetune;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Builder
public class FineTuneRequest {

  @NonNull
  @JsonProperty("training_file")
  private String trainingFile;

  @JsonInclude(Include.NON_NULL)
  @JsonProperty("validation_file")
  private String validationFile;

  @JsonInclude(Include.NON_NULL)
  private String model;

  @JsonInclude(Include.NON_NULL)
  @JsonProperty("n_epochs")
  private Integer numberEpochs;

  @JsonInclude(Include.NON_NULL)
  @JsonProperty("batch_size")
  private Integer batchSize;

  @JsonInclude(Include.NON_NULL)
  @JsonProperty("learning_rate_multiplier")
  private Double learningRateMultiplier;

  @JsonInclude(Include.NON_NULL)
  @JsonProperty("prompt_loss_weight")
  private Double promptLossWeight;

  @JsonInclude(Include.NON_NULL)
  @JsonProperty("compute_classification_metrics")
  private Boolean computeClassificationMetrics;

  @JsonInclude(Include.NON_NULL)
  @JsonProperty("classification_n_classes")
  private Integer classificationNClasses;

  @JsonInclude(Include.NON_NULL)
  @JsonProperty("classification_positive_class")
  private String classificationPositiveClass;

  @JsonInclude(Include.NON_NULL)
  @JsonProperty("classification_betas")
  private List<Double> classificationBetas;

  @JsonInclude(Include.NON_NULL)
  private String suffix;

}