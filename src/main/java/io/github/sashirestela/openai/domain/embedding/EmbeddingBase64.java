package io.github.sashirestela.openai.domain.embedding;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
public class EmbeddingBase64 {

    private Integer index;
    private String embedding;
    private String object;

}
