package io.github.sashirestela.openai.domain.embedding;

import io.github.sashirestela.openai.domain.OpenAIUsage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@NoArgsConstructor
@Getter
@ToString
public class EmbeddingFloatResponse {

    private String object;
    private List<EmbeddingFloat> data;
    private String model;
    private OpenAIUsage usage;

}
