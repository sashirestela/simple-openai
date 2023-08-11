package io.github.sashirestela.openai.domain.chat;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class Usage {

  @JsonProperty("prompt_tokens")
  private int promptTokens;

  @JsonProperty("completion_tokens")
  private int completionTokens;

  @JsonProperty("total_tokens")
  private int totalTokens;

}