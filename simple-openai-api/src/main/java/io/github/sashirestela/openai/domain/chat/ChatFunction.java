package io.github.sashirestela.openai.domain.chat;

import java.util.function.Function;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.github.sashirestela.openai.domain.chat.serializer.ParametersSerializer;

public class ChatFunction {
  private String name;
  private String description;
  @JsonSerialize(using = ParametersSerializer.class)
  private Class<?> parameters;
  @JsonIgnore
  Function<Object, Object> functionToExecute;

  public ChatFunction() {}

  public ChatFunction(String name,
                      String description,
                      Class<?> parameters,
                      Function<Object, Object> functionToExecute) {
    this.name = name;
    this.description = description;
    this.parameters = parameters;
    this.functionToExecute = functionToExecute;
  }

  private ChatFunction(Builder builder) {
    this.name = builder.name;
    this.description = builder.description;
    this.parameters = builder.parameters;
    this.functionToExecute = builder.functionToExecute;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public Class<?> getParameters() {
    return parameters;
  }

  public Function<Object, Object> getFunctionToExecute() {
    return functionToExecute;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private String name;
    private String description;
    private Class<?> parameters;
    Function<Object, Object> functionToExecute;

    public Builder() {}

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder description(String description) {
      this.description = description;
      return this;
    }

    public <T> Builder functionToExecute(Class<T> parametersClass, Function<T, Object> functionToExecute) {
      this.parameters = parametersClass != null ? parametersClass : Empty.class;
      this.functionToExecute = (Function<Object, Object>) functionToExecute;
      return this;
    }

    public ChatFunction build() {
      ChatFunction chatFunction = new ChatFunction(this);
      validate(chatFunction);
      return chatFunction;
    }

    private void validate(ChatFunction chatFunction) {
      if (chatFunction.name == null) {
        throw new RuntimeException("The name is required for ChatFunction.");
      }
      if (chatFunction.functionToExecute == null) {
        throw new RuntimeException("The functionToExecute is required for ChatFunction.");
      }
    }
  }

  class Empty {}
}