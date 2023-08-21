package io.github.sashirestela.openai.domain.file;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class FileDeletedResponse {

  private String id;

  private String object;

  private Boolean deleted;

}