package io.github.sashirestela.openai.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class OpenAIGeneric<T> {

  private String object;

  private List<T> data;

}