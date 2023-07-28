package io.github.sashirestela.openai.domain.chat;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Choice {
  
  private int index;
  @JsonAlias({"delta"})
  private ChatMessage message;
  @JsonProperty("finish_reason")
  private String finishReason;

  public Choice() {}

  public Choice(int index,
                ChatMessage message,
                String finishReason) {
    this.index = index;
    this.message = message;
    this.finishReason = finishReason;
  }
  
  public int getIndex() {
  	return index;
  }
  
  public ChatMessage getMessage() {
  	return message;
  }
  
  public String getFinishReason() {
  	return finishReason;
  }
}