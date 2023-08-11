package io.github.sashirestela.openai.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class OpenAIError {

  private ErrorDetail error;

  @NoArgsConstructor
  @AllArgsConstructor
  @Getter
  @ToString
  public static class ErrorDetail {

    private String message;

    private String type;

    private String param;

    private String code;

  }

}