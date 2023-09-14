package io.github.sashirestela.openai.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
public class OpenAIDeletedResponse {

  private String id;

  private String object;

  private Boolean deleted;

}