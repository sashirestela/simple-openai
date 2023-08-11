package io.github.sashirestela.openai.domain.chat;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonInclude.Include;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Builder
public class ChatRequest {

  @NonNull
  private String model;

  @NonNull
  private List<ChatMessage> messages;

  @JsonInclude(Include.NON_NULL)
  private List<ChatFunction> functions;

  @JsonInclude(Include.NON_NULL)
  @JsonProperty("function_call")
  private String functionCall;

  private double temperature;

  @JsonProperty("top_p")
  private double topP;

  private int n;

  private boolean stream;

  private List<String> stop;

  @JsonProperty("max_tokens")
  private int maxTokens;

  @JsonProperty("presence_penalty")
  private double presencePenalty;

  @JsonProperty("frequency_penalty")
  private double frequencyPenalty;

  @JsonProperty("logit_bias")
  private Map<String, Integer> logitBias;

  private String user;

  public void setStream(boolean stream) {
    this.stream = stream;
  }

}