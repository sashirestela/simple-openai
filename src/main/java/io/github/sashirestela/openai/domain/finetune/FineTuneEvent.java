package io.github.sashirestela.openai.domain.finetune;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class FineTuneEvent {

  private String object;

  @JsonProperty("created_at")
  private Long createdAt;

  private String level;

  private String message;

}