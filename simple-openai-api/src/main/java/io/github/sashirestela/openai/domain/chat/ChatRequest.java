package io.github.sashirestela.openai.domain.chat;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class ChatRequest {

  private String model;

  private List<ChatMessage> messages;

  private double temperature = 1.0;

  private boolean stream = false;

  @JsonProperty("max_tokens")
  private int maxTokens = Integer.MAX_VALUE;

  @JsonInclude(Include.NON_NULL)
  private List<ChatFunction> functions;

  @JsonInclude(Include.NON_NULL)
  @JsonProperty("function_call")
  private String functionCall;

  public ChatRequest() {
  }

  public ChatRequest(String model, List<ChatMessage> messages, float temperature, boolean stream, int maxTokens,
      List<ChatFunction> functions, String functionCall) {
    this.model = model;
    this.messages = messages;
    this.temperature = temperature;
    this.stream = stream;
    this.maxTokens = maxTokens;
    this.functions = functions;
    this.functionCall = functionCall;
  }

  private ChatRequest(Builder builder) {
    this.model = builder.model;
    this.messages = builder.messages;
    this.temperature = builder.temperature;
    this.stream = builder.stream;
    this.maxTokens = builder.maxTokens;
    this.functions = builder.functions;
    this.functionCall = builder.functionCall;
  }

  public String getModel() {
    return model;
  }

  public List<ChatMessage> getMessages() {
    return messages;
  }

  public double getTemperature() {
    return temperature;
  }

  public boolean isStream() {
    return stream;
  }

  public void setStream(boolean stream) {
    this.stream = stream;
  }

  public int getMaxTokens() {
    return maxTokens;
  }

  public List<ChatFunction> getFunctions() {
    return functions;
  }

  public String getFunctionCall() {
    return functionCall;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private String model;
    private List<ChatMessage> messages;
    private double temperature;
    private boolean stream;
    private int maxTokens;
    private List<ChatFunction> functions;
    private String functionCall;

    public Builder() {
    }

    public Builder model(String model) {
      this.model = model;
      return this;
    }

    public Builder messages(List<ChatMessage> messages) {
      this.messages = messages;
      return this;
    }

    public Builder temperature(double temperature) {
      this.temperature = temperature;
      return this;
    }

    public Builder stream(boolean stream) {
      this.stream = stream;
      return this;
    }

    public Builder maxTokens(int maxTokens) {
      this.maxTokens = maxTokens;
      return this;
    }

    public Builder functions(List<ChatFunction> functions) {
      this.functions = functions;
      return this;
    }

    public Builder functionCall(String functionCall) {
      this.functionCall = functionCall;
      return this;
    }

    public ChatRequest build() {
      ChatRequest chatRequest = new ChatRequest(this);
      validate(chatRequest);
      return chatRequest;
    }

    private void validate(ChatRequest chatRequest) {
      if (chatRequest.model == null) {
        throw new RuntimeException("The model is required for ChatRequest.");
      }
      if (chatRequest.messages == null) {
        throw new RuntimeException("The messages are required for ChatRequest.");
      }
    }
  }

  @Override
  public String toString() {
    return "ChatRequest [model=" + model + ", messages=" + messages + ", temperature=" + temperature + ", stream="
        + stream + ", maxTokens=" + maxTokens + ", functions=" + functions + ", functionCall=" + functionCall + "]";
  }
}