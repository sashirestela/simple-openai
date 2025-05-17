package io.github.sashirestela.openai.domain.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.github.sashirestela.openai.domain.chat.ChatRequest.ServiceTier;
import io.github.sashirestela.openai.domain.response.ResponseRequest.Truncation;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Response {

    private Long createdAt;
    private ResponseError error;
    private String id;
    private IncompleteDetails incompleteDetails;
    private String instructions;
    private Long maxOutputTokens;
    private Map<String, String> metadata;
    private String model;
    private String object;
    @JsonDeserialize(contentUsing = ItemDeserializer.class)
    private List<Input.Item> output;
    private Boolean parallelToolCalls;
    private String previousResponseId;
    private Reasoning reasoning;
    private ServiceTier serviceTier;
    private ResponseStatus status;
    private Double temperature;
    private ResponseText text;
    private Object toolChoice;
    private List<ResponseTool> tools;
    private Double topP;
    private Truncation truncation;
    private ResponseUsage usage;
    private String user;

    public String outputText() {
        return output.stream()
                .filter(item -> (item instanceof Input.Item.OutputMessageItem))
                .filter(item -> ((Input.Item.OutputMessageItem) item).getContent() != null)
                .flatMap(item -> ((Input.Item.OutputMessageItem) item).getContent().stream())
                .filter(content -> (content instanceof Input.OutputContent.TextOutputContent))
                .map(content -> ((Input.OutputContent.TextOutputContent) content).getText())
                .collect(Collectors.joining());
    }

    @NoArgsConstructor
    @Getter
    @ToString
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ResponseError {

        private String code;
        private String message;

    }

    @NoArgsConstructor
    @Getter
    @ToString
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class IncompleteDetails {

        private String reason;

    }

    @NoArgsConstructor
    @Getter
    @ToString
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ResponseUsage {

        private Integer inputTokens;
        private InputTokenDetails inputTokenDetails;
        private Integer outputTokens;
        private OutputTokenDetails outputTokenDetails;
        private Integer totalTokens;

        @NoArgsConstructor
        @Getter
        @ToString
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class InputTokenDetails {

            private Integer cachedTokens;

        }

        @NoArgsConstructor
        @Getter
        @ToString
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class OutputTokenDetails {

            private Integer reasoningTokens;

        }

    }

    public enum ResponseStatus {

        @JsonProperty("completed")
        COMPLETED,

        @JsonProperty("failed")
        FAILED,

        @JsonProperty("in_progress")
        IN_PROGRESS,

        @JsonProperty("incomplete")
        INCOMPLETE;

    }

}
