package io.github.sashirestela.openai.domain.image;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class ImageResponse {
  
  private String url;

  @JsonProperty("b64_json")
  private String b64Json;

}