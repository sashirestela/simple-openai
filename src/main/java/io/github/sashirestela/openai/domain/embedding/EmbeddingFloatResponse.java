package io.github.sashirestela.openai.domain.embedding;

import io.github.sashirestela.openai.domain.OpenAIUsage;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
public class EmbeddingFloatResponse {

    private String object;
    private List<EmbeddingFloat> data;
    private String model;
    private OpenAIUsage usage;

}