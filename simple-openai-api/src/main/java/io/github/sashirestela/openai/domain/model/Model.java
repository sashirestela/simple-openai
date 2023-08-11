package io.github.sashirestela.openai.domain.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class Model {

  private String id;

  private String object;

  private long created;

  @JsonProperty("owned_by")
  private String ownedBy;

  private List<ModelPermission> permission;

  private String root;

  private String parent;

}