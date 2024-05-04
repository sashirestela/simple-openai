package io.github.sashirestela.openai.domain.embedding;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@NoArgsConstructor
@Getter
@ToString
public class EmbeddingFloat {

    private Integer index;
    private List<Double> embedding;
    private String object;

}
