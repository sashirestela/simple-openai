package io.github.sashirestela.openai.domain.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
public class ModelResponse {

  private String id;

  private String object;

  private long created;

  @JsonProperty("owned_by")
  private String ownedBy;

  private List<Permission> permission;

  private String root;

  private String parent;

}