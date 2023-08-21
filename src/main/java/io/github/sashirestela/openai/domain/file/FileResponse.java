package io.github.sashirestela.openai.domain.file;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class FileResponse {

  private String id;

  private String object;

  private String purpose;

  private String filename;

  private Integer bytes;

  @JsonProperty("created_at")
  private Double createdAt;

  private String status;

  @JsonProperty("status_details")
  private String statusDetails;

}