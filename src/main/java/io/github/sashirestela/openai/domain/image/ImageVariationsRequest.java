package io.github.sashirestela.openai.domain.image;

import java.io.File;

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
public class ImageVariationsRequest extends AbstractImageRequest {

  @NonNull
  private File image;

}