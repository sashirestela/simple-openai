package io.github.sashirestela.openai.domain.audio;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@NoArgsConstructor
@Getter
@ToString
public class Transcription {

    private String task;
    private String language;
    private Double duration;
    private String text;
    private List<AudioWord> words;
    private List<Segment> segments;

    @NoArgsConstructor
    @Getter
    @ToString
    public static class AudioWord {

        private String word;
        private Double start;
        private Double end;

    }

    @NoArgsConstructor
    @Getter
    @ToString
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Segment {

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

}
