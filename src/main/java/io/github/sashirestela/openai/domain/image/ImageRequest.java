package io.github.sashirestela.openai.domain.image;

import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class ImageRequest extends AbstractImageRequest {

  @NonNull
  private String prompt;

}