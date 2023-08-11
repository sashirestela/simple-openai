package io.github.sashirestela.openai.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class ModelPermission {

  private String id;

  private String object;

  private long created;

  @JsonProperty("allow_create_engine")
  private boolean allowCreateEngine;

  @JsonProperty("allow_sampling")
  private boolean allowSampling;

  @JsonProperty("allow_logprobs")
  private boolean allowLogProbs;

  @JsonProperty("allow_search_indices")
  private boolean allowSearchIndices;

  @JsonProperty("allow_view")
  private boolean allowView;

  @JsonProperty("allow_fine_tuning")
  private boolean allowFineTuning;

  private String organization;

  private String group;

  @JsonProperty("is_blocking")
  private boolean isBlocking;

}