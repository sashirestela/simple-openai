package io.github.sashirestela.openai.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelPermission {
  private String id;
  private String object;
  private long created;
  @JsonProperty("allow_create_engine")
  private boolean allowCreateEngine;
  @JsonProperty("allow_sampling")
  private boolean allowSampling;
  @JsonProperty("allow_logprobs")
  private boolean allowLogProbs;
  @JsonProperty("allow_search_indices")
  private boolean allowSearchIndices;
  @JsonProperty("allow_view")
  private boolean allowView;
  @JsonProperty("allow_fine_tuning")
  private boolean allowFineTuning;
  private String organization;
  private String group;
  @JsonProperty("is_blocking")
  private boolean isBlocking;

  public ModelPermission() {}

  public ModelPermission(String id,
                         String object,
                         long created,
                         boolean allowCreateEngine,
                         boolean allowSampling,
                         boolean allowLogProbs,
                         boolean allowSearchIndices,
                         boolean allowView,
                         boolean allowFineTuning,
                         String organization,
                         String group,
                         boolean isBlocking) {
    this.id = id;
    this.object = object;
    this.created = created;
    this.allowCreateEngine = allowCreateEngine;
    this.allowSampling = allowSampling;
    this.allowLogProbs = allowLogProbs;
    this.allowSearchIndices = allowSearchIndices;
    this.allowView = allowView;
    this.allowFineTuning = allowFineTuning;
    this.organization = organization;
    this.group = group;
    this.isBlocking = isBlocking;
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
  
  public boolean isAllowCreateEngine() {
  	return allowCreateEngine;
  }
  
  public boolean isAllowSampling() {
  	return allowSampling;
  }
  
  public boolean isAllowLogProbs() {
  	return allowLogProbs;
  }
  
  public boolean isAllowSearchIndices() {
  	return allowSearchIndices;
  }
  
  public boolean isAllowView() {
  	return allowView;
  }
  
  public boolean isAllowFineTuning() {
  	return allowFineTuning;
  }
  
  public String getOrganization() {
  	return organization;
  }
  
  public String getGroup() {
  	return group;
  }
  
  public boolean isBlocking() {
  	return isBlocking;
  }
}