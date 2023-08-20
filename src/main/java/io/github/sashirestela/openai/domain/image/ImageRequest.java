package io.github.sashirestela.openai.domain.image;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.SuperBuilder;;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@SuperBuilder
public class ImageRequest extends AbstractImageRequest {

  @NonNull
  private String prompt;

}