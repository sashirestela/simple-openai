package io.github.sashirestela.openai.domain.model;

import java.util.List;

public class ModelResponse {
  private String object;
  private List<Model> data;

  public ModelResponse() {}

  public ModelResponse(String object,
                       List<Model> data) {
    this.object = object;
    this.data = data;
  }
  
  public String getObject() {
  	return object;
  }
  public List<Model> getData() {
  	return data;
  }
}