package io.github.sashirestela.openai.domain.embedding;

import io.github.sashirestela.openai.common.Usage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@NoArgsConstructor
@Getter
@ToString
public class Embedding<T> {

    private String object;
    private List<T> data;
    private String model;
    private Usage usage;

}
