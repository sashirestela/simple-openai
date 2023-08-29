package io.github.sashirestela.openai.domain.file;

import java.nio.file.Path;

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
public class FileRequest {

  @NonNull
  private Path file;

  @NonNull
  private String purpose;

}