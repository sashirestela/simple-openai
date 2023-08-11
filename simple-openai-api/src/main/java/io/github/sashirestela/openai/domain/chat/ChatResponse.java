package io.github.sashirestela.openai.domain.chat;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class ChatResponse {

  private String id;

  private String object;

  private long created;

  private String model;

  private List<Choice> choices;

  private Usage usage;

  public ChatMessage firstMessage() {
    return getChoices().get(0).getMessage();
  }

  public String firstContent() {
    return firstMessage().getContent();
  }

}