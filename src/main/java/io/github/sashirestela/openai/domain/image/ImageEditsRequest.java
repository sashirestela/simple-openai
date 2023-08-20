package io.github.sashirestela.openai.domain.image;

import java.io.File;

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
  private File image;

  @JsonInclude(Include.NON_NULL)
  private File mask;

  @NonNull
  private String prompt;

}