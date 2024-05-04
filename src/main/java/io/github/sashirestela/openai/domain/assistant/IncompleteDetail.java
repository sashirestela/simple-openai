package io.github.sashirestela.openai.domain.assistant;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class IncompleteDetail {

    private IncompleteDetailReason reason;

    public enum IncompleteDetailReason {

        @JsonProperty("content_filter")
        CONTENT_FILTER,

        @JsonProperty("max_tokens")
        MAX_TOKENS,

        @JsonProperty("run_cancelled")
        RUN_CANCELLED,

        @JsonProperty("run_expired")
        RUN_EXPIRED,

        @JsonProperty("run_failed")
        RUN_FAILED,

        @JsonProperty("max_completion_tokens")
        MAX_COMPLETION_TOKENS,

        @JsonProperty("max_prompt_tokens")
        MAX_PROMPT_TOKENS;

    }

}
