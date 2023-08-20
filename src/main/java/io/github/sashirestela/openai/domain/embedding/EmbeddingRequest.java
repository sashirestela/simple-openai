package io.github.sashirestela.openai.domain.embedding;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Builder
public class EmbeddingRequest {

  @NonNull
  private String model;

  @NonNull
  private List<String> input;

  @JsonInclude(Include.NON_NULL)
  private String user;

}