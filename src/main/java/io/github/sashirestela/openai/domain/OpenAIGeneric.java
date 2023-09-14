package io.github.sashirestela.openai.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
public class OpenAIGeneric<T> {

  private String object;

  private Long created;

  private List<T> data;

  @JsonProperty("next_starting_after")
  private String nextStartingAfter;

  @JsonProperty("has_more")
  private boolean hasMore;

}