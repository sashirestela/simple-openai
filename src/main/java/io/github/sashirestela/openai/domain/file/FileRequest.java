package io.github.sashirestela.openai.domain.file;

import java.nio.file.Path;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@AllArgsConstructor
@Getter
@Builder
public class FileRequest {

  @NonNull
  private Path file;

  @NonNull
  private String purpose;

}