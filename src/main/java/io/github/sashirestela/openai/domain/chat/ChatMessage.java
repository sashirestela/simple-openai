package io.github.sashirestela.openai.domain.chat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.github.sashirestela.openai.SimpleUncheckedException;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
public class ChatMessage {

  @NonNull
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

  @Builder
  public ChatMessage(@NonNull Role role, String content, String name, ChatFunctionCall functionCall) {
    this.role = role;
    this.content = content;
    this.name = name;
    this.functionCall = functionCall;
    validate();
  }

  private void validate() {
    if (role != Role.ASSISTANT && content == null) {
      throw new SimpleUncheckedException("The content is required for ChatMessage when role is other than assistant.",
          null, null);
    }
    if (role == Role.FUNCTION && name == null) {
      throw new SimpleUncheckedException("The name is required for ChatMessage when role is function.",
          null, null);
    }
  }

}