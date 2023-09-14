package io.github.sashirestela.openai.domain.chat;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
public class Choice {

  private Integer index;

  @JsonAlias({ "delta" })
  private ChatMessage message;

  @JsonProperty("finish_reason")
  private String finishReason;

}