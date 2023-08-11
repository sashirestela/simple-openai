package io.github.sashirestela.openai.domain.chat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
public class ChatMessage {

  private Role role;

  private String content;

  @JsonInclude(Include.NON_NULL)
  private String name;

  @JsonInclude(Include.NON_NULL)
  @JsonProperty("function_call")
  private ChatFunctionCall functionCall;

  public ChatMessage(Role role, String content) {
    this.role = role;
    this.content = content;
    validate();
  }

  public ChatMessage(Role role, String content, String name) {
    this.role = role;
    this.content = content;
    this.name = name;
    validate();
  }

  public ChatMessage(Role role, String content, String name, ChatFunctionCall functionCall) {
    this.role = role;
    this.content = content;
    this.name = name;
    this.functionCall = functionCall;
    validate();
  }

  private void validate() {
    if (role == null) {
      throw new RuntimeException("The role is required for ChatMessage.");
    }
    if (role != Role.ASSISTANT && content == null) {
      throw new RuntimeException("The content is required for ChatMessage when role is other than assistant.");
    }
    if (role == Role.FUNCTION && name == null) {
      throw new RuntimeException("The name is required for ChatMessage when role is function.");
    }
  }

}