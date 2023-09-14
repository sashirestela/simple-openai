package io.github.sashirestela.openai.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
public class OpenAIUsage {

  @JsonProperty("prompt_tokens")
  private Integer promptTokens;

  @JsonProperty("completion_tokens")
  private Integer completionTokens;

  @JsonProperty("total_tokens")
  private Integer totalTokens;

}