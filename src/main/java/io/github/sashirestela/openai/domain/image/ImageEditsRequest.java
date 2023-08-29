package io.github.sashirestela.openai.domain.image;

import java.nio.file.Path;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@SuperBuilder
public class ImageEditsRequest extends AbstractImageRequest {

  @NonNull
  private Path image;

  @JsonInclude(Include.NON_NULL)
  private Path mask;

  @NonNull
  private String prompt;

}