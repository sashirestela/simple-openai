package io.github.sashirestela.openai.domain.moderation;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class Moderation {

  private Boolean flagged;

  private Category categories;

  @JsonProperty("category_scores")
  private CategoryScore categoryScores;

}