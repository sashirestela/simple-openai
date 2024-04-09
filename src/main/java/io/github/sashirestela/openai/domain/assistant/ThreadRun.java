package io.github.sashirestela.openai.domain.assistant;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.github.sashirestela.cleverclient.util.UnixTimestampDeserializer;
import io.github.sashirestela.openai.domain.OpenAIUsage;
import io.github.sashirestela.openai.domain.ToolCall;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

/**
 * Represents an execution run on a thread.
 */
@NoArgsConstructor
@Getter
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ThreadRun {

    private String id;
    private String object;
    @JsonDeserialize(using = UnixTimestampDeserializer.class)
    private ZonedDateTime createdAt;
    private String threadId;
    private String assistantId;
    private String status;
    private ThreadRunAction requiredAction;
    private Error lastError;
    @JsonDeserialize(using = UnixTimestampDeserializer.class)
    private ZonedDateTime expiresAt;
    @JsonDeserialize(using = UnixTimestampDeserializer.class)
    private ZonedDateTime startedAt;
    @JsonDeserialize(using = UnixTimestampDeserializer.class)
    private ZonedDateTime cancelledAt;
    @JsonDeserialize(using = UnixTimestampDeserializer.class)
    private ZonedDateTime failedAt;
    @JsonDeserialize(using = UnixTimestampDeserializer.class)
    private ZonedDateTime completedAt;
    private String model;
    private String instructions;
    private List<AssistantTool> tools;
    private List<String> fileIds;
    private Map<String, String> metadata;
    private OpenAIUsage usage;
    private Double temperature;

    public final class Status {

        private Status() {
        }

        public static final String QUEUED = "queued";
        public static final String IN_PROGRESS = "in_progress";
        public static final String REQUIRES_ACTION = "requires_action";
        public static final String CANCELLING = "cancelling";
        public static final String CANCELLED = "cancelled";
        public static final String FAILED = "failed";
        public static final String COMPLETED = "completed";
        public static final String EXPIRED = "expired";

    }

    public boolean isActionRequired() {
        return (requiredAction != null);
    }

    public List<ToolCall> getRequiredToolCalls() {
        if (requiredAction != null
                && requiredAction.getSubmitToolOutputs() != null
                && requiredAction.getSubmitToolOutputs().getToolCalls() != null) {
            return requiredAction.getSubmitToolOutputs().getToolCalls();
        } else {
            return List.of();
        }
    }

    @NoArgsConstructor
    @Getter
    @ToString
    public static class Error {

        private String code;
        private String message;

    }

}
