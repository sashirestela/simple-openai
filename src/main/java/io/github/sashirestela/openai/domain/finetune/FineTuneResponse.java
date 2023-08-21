package io.github.sashirestela.openai.domain.finetune;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.github.sashirestela.openai.domain.file.FileResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class FineTuneResponse {

  private String id;

  private String object;

  @JsonProperty("created_at")
  private Long createdAt;

  @JsonProperty("updated_at")
  private Long updatedAt;

  private String model;

  @JsonProperty("fine_tuned_model")
  private String fineTunedModel;

  @JsonProperty("organization_id")
  private String organizationId;

  private String status;

  private HyperParams hyperparams;

  @JsonProperty("training_files")
  private List<FileResponse> trainingFiles;

  @JsonProperty("validation_files")
  private List<FileResponse> validationFiles;

  @JsonProperty("result_files")
  private List<FileResponse> resultFiles;

  private List<FineTuneEvent> events;

}