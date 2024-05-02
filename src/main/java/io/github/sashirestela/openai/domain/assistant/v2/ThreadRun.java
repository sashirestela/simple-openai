package io.github.sashirestela.openai.domain.assistant.v2;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.github.sashirestela.openai.domain.OpenAIUsage;
import io.github.sashirestela.openai.function.AbstractToolCall;
import io.github.sashirestela.openai.function.FunctionCall;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
@Getter
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ThreadRun {

    private String id;
    private String object;
    private Integer createdAt;
    private String threadId;
    private String assistantId;
    private RunStatus status;
    private RequiredAction requiredAction;
    private LastError lastError;
    private Integer expiresAt;
    private Integer startedAt;
    private Integer cancelledAt;
    private Integer failedAt;
    private Integer completedAt;
    private ThreadMessage.IncompleteDetail incompleteDetails;
    private String model;
    private String instructions;
    private List<AssistantTool> tools;
    private Map<String, String> metadata;
    private OpenAIUsage usage;
    private Double temperature;
    private Double topP;
    private Integer maxPromptTokens;
    private Integer maxCompletionTokens;
    private ThreadRunRequest.TruncationStrategy truncationStrategy;
    private Object toolChoice;
    private Object responseFormat;

    public enum RunStatus {

        @JsonProperty("queued")
        QUEUED,

        @JsonProperty("in_progress")
        IN_PROGRESS,

        @JsonProperty("requires_action")
        REQUIRES_ACTION,

        @JsonProperty("cancelling")
        CANCELLING,

        @JsonProperty("cancelled")
        CANCELLED,

        @JsonProperty("failed")
        FAILED,

        @JsonProperty("completed")
        COMPLETED,

        @JsonProperty("expired")
        EXPIRED;

    }

    @NoArgsConstructor
    @Getter
    @ToString
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class RequiredAction {

        private RequiredActionType type;
        private SubmitToolOutput submitToolOutputs;

        public enum RequiredActionType {

            @JsonProperty("submit_tool_outputs")
            SUBMIT_TOOL_OUTPUTS;

        }

        @NoArgsConstructor
        @Getter
        @ToString
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class SubmitToolOutput {

            private List<ThreadRunToolCall> toolCalls;

            @NoArgsConstructor
            @Getter
            @ToString
            @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
            public static class ThreadRunToolCall extends AbstractToolCall {

                private ToolType type;

                public ThreadRunToolCall(String id, ToolType type, FunctionCall function) {
                    this.id = id;
                    this.type = type;
                    this.function = function;
                }

            }

        }

    }

    @NoArgsConstructor
    @Getter
    @ToString
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class LastError {

        private ErrorCode code;
        private String message;

        public enum ErrorCode {

            @JsonProperty("server_error")
            SERVER_ERROR,

            @JsonProperty("rate_limit_exceeded")
            RATE_LIMIT_EXCEEDED,

            @JsonProperty("invalid_prompt")
            INVALID_PROMPT;
        }

    }

}
