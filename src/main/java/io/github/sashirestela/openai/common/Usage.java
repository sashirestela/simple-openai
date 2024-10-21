package io.github.sashirestela.openai.common;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Usage {

    private Integer promptTokens;
    private Integer completionTokens;
    private Integer totalTokens;
    private CompletionTokensDetails completionTokensDetails;
    private PromptTokensDetails promptTokensDetails;

    @NoArgsConstructor
    @Getter
    @ToString
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class CompletionTokensDetails {

        private Integer audioTokens;
        private Integer reasoningTokens;

    }

    @NoArgsConstructor
    @Getter
    @ToString
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class PromptTokensDetails {

        private Integer audioTokens;
        private Integer cachedTokens;

    }

}
