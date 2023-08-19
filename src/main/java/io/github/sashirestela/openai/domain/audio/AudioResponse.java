package io.github.sashirestela.openai.domain.audio;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class AudioResponse {

  private String text;
  
  private String task;

  private String language;

  private Double duration;

  private List<Segment> segments;

}