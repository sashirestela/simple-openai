package io.github.sashirestela.openai.domain.audio;

import java.nio.file.Path;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

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
public class AudioTranslateRequest {

  @NonNull
  protected Path file;

  @NonNull
  protected String model;

  @JsonInclude(Include.NON_NULL)
  protected String prompt;

  @JsonInclude(Include.NON_NULL)
  @JsonProperty("response_format")
  protected AudioRespFmt responseFormat;

  @JsonInclude(Include.NON_NULL)
  protected Double temperature;

  public void setResponseFormat(AudioRespFmt responseFormat) {
    this.responseFormat = responseFormat;
  }

}