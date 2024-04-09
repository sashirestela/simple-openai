package io.github.sashirestela.openai.domain.audio;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
public class AudioWord {

    private String word;
    private Double start;
    private Double end;

}
