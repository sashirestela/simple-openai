package io.github.sashirestela.openai.domain.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Model {
  private String id;
  private String object;
  private long created;
  @JsonProperty("owned_by")
  private String ownedBy;
  private List<ModelPermission> permission;
  private String root;
  private String parent;

  public Model() {}

  public Model(String id,
               String object,
               long created,
               String ownedBy,
               List<ModelPermission> permission,
               String root,
               String parent) {
    this.id = id;
    this.object = object;
    this.created = created;
    this.ownedBy = ownedBy;
    this.permission = permission;
    this.root = root;
    this.parent = parent;
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
  
  public String getOwnedBy() {
  	return ownedBy;
  }
  
  public List<ModelPermission> getPermission() {
  	return permission;
  }
  
  public String getRoot() {
  	return root;
  }
  
  public String getParent() {
  	return parent;
  }
}