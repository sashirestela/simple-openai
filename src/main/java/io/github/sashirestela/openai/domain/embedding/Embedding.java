package io.github.sashirestela.openai.domain.embedding;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class Embedding {
  
  private Integer index;

  private String object;

  private List<Double> embedding;

}