package io.github.sashirestela.openai.domain.audio;

import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Segment {

    private Integer id;
    private Integer seek;
    private Double start;
    private Double end;
    private String text;
    private List<Integer> tokens;
    private Double temperature;
    private Double avgLogprob;
    private Double compressionRatio;
    private Double noSpeechProb;

}