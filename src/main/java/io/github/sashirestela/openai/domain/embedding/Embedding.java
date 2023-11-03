package io.github.sashirestela.openai.domain.embedding;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
public class Embedding {

    private Integer index;

    private String object;

    private List<Double> embedding;

}