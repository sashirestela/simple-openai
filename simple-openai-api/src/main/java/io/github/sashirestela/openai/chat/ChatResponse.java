package io.github.sashirestela.openai.chat;

import java.util.List;

public class ChatResponse {
  
  private String id;
  private String object;
  private long created;
  private String model;
  private List<Choice> choices;
  private Usage usage;

  public ChatResponse() {}

  public ChatResponse(String id,
                      String object,
                      long created,
                      String model,
                      List<Choice> choices,
                      Usage usage) {
    this.id = id;
    this.object = object;
    this.created = created;
    this.model = model;
    this.choices = choices;
    this.usage = usage;
  }
  
  public String getId() {
  	return id;
  }
  
  public String getObject() {
  	return object;
  }
  
  public long getCreated() {
  	return created;
  }
  
  public String getModel() {
  	return model;
  }
  
  public List<Choice> getChoices() {
  	return choices;
  }
  
  public Usage getUsage() {
  	return usage;
  }

  public ChatMessage firstMessage() {
    return getChoices().get(0).getMessage();
  }

  public String firstContent() {
    return firstMessage().getContent();
  }
}