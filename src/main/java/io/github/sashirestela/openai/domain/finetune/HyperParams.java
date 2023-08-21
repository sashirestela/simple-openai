package io.github.sashirestela.openai.domain.finetune;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class HyperParams {

  @JsonProperty("n_epochs")
  private Integer nEpochs;

  @JsonProperty("batch_size")
  private Integer batchSize;

  @JsonProperty("prompt_loss_weight")
  private Double promptLossWeight;

  @JsonProperty("learning_rate_multiplier")
  private Double learningRateMultiplier;

  @JsonProperty("compute_classification_metrics")
  private Boolean computeClassificationMetrics;

  @JsonProperty("classification_positive_class")
  private String classificationPositiveClass;

  @JsonProperty("classification_n_classes")
  private Integer classificationNClasses;

}