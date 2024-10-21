package io.github.sashirestela.openai.domain.chat;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.github.sashirestela.openai.common.Usage;
import io.github.sashirestela.openai.domain.chat.ChatMessage.ResponseMessage;
import io.github.sashirestela.openai.domain.chat.ChatRequest.ServiceTier;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@NoArgsConstructor
@Getter
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Chat {

    private String id;
    private String object;
    private Long created;
    private String model;
    private ServiceTier serviceTier;
    private String systemFingerprint;
    private List<Choice> choices;
    private Usage usage;

    public ResponseMessage firstMessage() {
        return getChoices().get(0).getMessage();
    }

    public String firstContent() {
        return firstMessage().getContent();
    }

    @NoArgsConstructor
    @Getter
    @Setter
    @ToString
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Choice {

        private Integer index;

        @JsonAlias({ "delta" })
        private ResponseMessage message;

        private String finishReason;

        private LogprobInfo logprobs;

        @NoArgsConstructor
        @Getter
        @ToString
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class LogprobInfo {

            private List<TokenLogprob> content;
            private List<TokenLogprob> refusal;

            @NoArgsConstructor
            @Getter
            @ToString
            @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
            public static class TokenLogprob {

                private String token;
                private Double logprob;
                private List<Integer> bytes;
                private List<TopLogprob> topLogprobs;

            }

            @NoArgsConstructor
            @Getter
            @ToString
            @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
            public static class TopLogprob {

                private String token;
                private Double logprob;
                private List<Integer> bytes;

            }

        }

    }

}
