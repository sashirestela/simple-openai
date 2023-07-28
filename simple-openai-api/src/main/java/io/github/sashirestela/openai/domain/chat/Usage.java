package io.github.sashirestela.openai.domain.chat;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Usage {
  
  @JsonProperty("prompt_tokens")
  private int promptTokens;
  @JsonProperty("completion_tokens")
  private int completionTokens;
  @JsonProperty("total_tokens")
  private int totalTokens;

  public Usage() {}

  public Usage(int promptTokens,
               int completionTokens,
               int totalTokens) {
    this.promptTokens = promptTokens;
    this.completionTokens = completionTokens;
    this.totalTokens = totalTokens;
  }
  
  public int getPromptTokens() {
  	return promptTokens;
  }
  
  public int getCompletionTokens() {
  	return completionTokens;
  }
  
  public int getTotalTokens() {
  	return totalTokens;
  }
}