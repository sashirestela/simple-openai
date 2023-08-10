package io.github.sashirestela.openai.domain;

public class OpenAIError {
  
  private ErrorDetail error;

  public OpenAIError() {
  }

  public OpenAIError(ErrorDetail error) {
    this.error = error;
  }

  public ErrorDetail getError() {
    return error;
  }

  public static class ErrorDetail {
    private String message;
    private String type;
    private String param;
    private String code;

    public ErrorDetail() {
    }

    public ErrorDetail(String message, String type, String param, String code) {
      this.message = message;
      this.type = type;
      this.param = param;
      this.code = code;
    }

    public String getMessage() {
      return message;
    }

    public String getType() {
      return type;
    }

    public String getParam() {
      return param;
    }

    public String getCode() {
      return code;
    }

    @Override
    public String toString() {
      return "ErrorDetail [message=" + message + ", type=" + type + ", param=" + param + ", code=" + code + "]";
    }
  }

  @Override
  public String toString() {
    return "OpenAIError [error=" + error + "]";
  }
}
