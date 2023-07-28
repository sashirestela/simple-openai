package io.github.sashirestela.openai.support;

public enum Url {
  
  OPENAI_CHAT_COMPLETIONS("https://api.openai.com/v1/chat/completions"),
  OPENAI_MODELS("https://api.openai.com/v1/models");

  public final String value;

  Url(String value) {
    this.value = value;
  }
}