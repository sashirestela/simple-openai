package io.github.sashirestela.openai.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OpenAIEvent {

  private final static String DATA_HEADER = "data: ";
  private final static String STREAM_DONE = "[DONE]";

  private String rawData;

  public boolean isActualData() {
    return rawData.startsWith(DATA_HEADER) && !rawData.contains(STREAM_DONE);
  }

  public String getActualData() {
    return rawData.replace(DATA_HEADER, "").strip();
  }

}