package io.github.sashirestela.openai.domain.moderation;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
@Builder
public class ModerationRequest {

  @NonNull
  private List<String> input;

  @JsonInclude(Include.NON_NULL)
  private String model;

}