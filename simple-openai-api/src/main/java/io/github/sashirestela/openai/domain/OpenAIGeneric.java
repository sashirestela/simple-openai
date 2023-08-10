package io.github.sashirestela.openai.domain;

import java.util.List;

public class OpenAIGeneric<T> {
  private String object;
  private List<T> data;

  public OpenAIGeneric() {
  }

  public OpenAIGeneric(String object, List<T> data) {
    this.object = object;
    this.data = data;
  }

  public String getObject() {
    return object;
  }

  public List<T> getData() {
    return data;
  }
}
