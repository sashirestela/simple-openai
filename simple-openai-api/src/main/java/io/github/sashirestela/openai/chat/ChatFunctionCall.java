package io.github.sashirestela.openai.chat;

public class ChatFunctionCall {
  private String name;
  private String arguments;

  public ChatFunctionCall() {}

  public ChatFunctionCall(String name,
                          String arguments) {
    this.name = name;
    this.arguments = arguments;
  }

  public String getName() {
    return name;
  }

  public String getArguments() {
    return arguments;
  }
}