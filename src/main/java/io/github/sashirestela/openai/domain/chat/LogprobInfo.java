package io.github.sashirestela.openai.domain.chat;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@NoArgsConstructor
@Getter
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class LogprobInfo {

    private List<TokenLogprob> content;

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
