package io.github.sashirestela.openai.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class OpenAIDeletedResponse {

  private String id;

  private String object;

  private Boolean deleted;

}