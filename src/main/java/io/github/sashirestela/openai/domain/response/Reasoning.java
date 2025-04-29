package io.github.sashirestela.openai.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.github.sashirestela.openai.domain.chat.ChatRequest.ReasoningEffort;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Getter
@ToString
@JsonInclude(Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Reasoning {

    private ReasoningEffort effort;
    private Summary summary;

    public static Reasoning of(ReasoningEffort effort) {
        return new Reasoning(effort, null);
    }

    public static Reasoning of(ReasoningEffort effort, Summary summary) {
        return new Reasoning(effort, summary);
    }

    public enum Summary {
        @JsonProperty("auto")
        AUTO,

        @JsonProperty("concise")
        CONCISE,

        @JsonProperty("detailed")
        DETAILED;
    }

}
