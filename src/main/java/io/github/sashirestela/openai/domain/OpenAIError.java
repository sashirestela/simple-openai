package io.github.sashirestela.openai.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
public class OpenAIError {

  private ErrorDetail error;

  @NoArgsConstructor
  @Getter
  @ToString
  public static class ErrorDetail {

    private String message;

    private String type;

    private String param;

    private String code;

  }

}