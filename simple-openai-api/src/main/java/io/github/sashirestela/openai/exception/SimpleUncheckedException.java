package io.github.sashirestela.openai.exception;

public class SimpleUncheckedException extends UncheckedException {

  public SimpleUncheckedException(String message, Object... parameters) {
    super(message, parameters);
  }

}
