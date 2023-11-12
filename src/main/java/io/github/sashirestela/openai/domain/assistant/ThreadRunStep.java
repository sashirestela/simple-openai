package io.github.sashirestela.openai.domain.assistant;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.github.sashirestela.cleverclient.util.UnixTimestampDeserializer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.ZonedDateTime;
import java.util.Map;

/**
 * Represents a step in execution of a run.
 *
 */
@NoArgsConstructor
@Getter
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ThreadRunStep {

    private String id;
    private String object;
    @JsonDeserialize(using = UnixTimestampDeserializer.class)
    private ZonedDateTime createdAt;
    private String assistantId;
    private String threadId;
    private String runId;
    private String type;
    private String status;
    private Details stepDetails;
    private ThreadRun.Error lastError;
    @JsonDeserialize(using = UnixTimestampDeserializer.class)
    private ZonedDateTime expiredAt;
    @JsonDeserialize(using = UnixTimestampDeserializer.class)
    private ZonedDateTime cancelledAt;
    @JsonDeserialize(using = UnixTimestampDeserializer.class)
    private ZonedDateTime failedAt;
    @JsonDeserialize(using = UnixTimestampDeserializer.class)
    private ZonedDateTime completedAt;
    private Map<String, String> metadata;

    public interface Type {
        String MESSAGE_CREATION = "message_creation";
        String TOOL_CALLS = "tool_calls";
    }

    public interface Status {
        String IN_PROGRESS = "in_progress";
        String CANCELLED = "cancelled";
        String FAILED = "failed";
        String COMPLETED = "completed";
        String EXPIRED = "expired";
    }

    @NoArgsConstructor
    @Getter
    @ToString
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Details {

        private String type;
        private MessageCreation messageCreation;
        private ToolCalls toolCalls;

    }

    @NoArgsConstructor
    @Getter
    @ToString
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class MessageCreation {

        private ThreadMessageId messageId;

    }

    @NoArgsConstructor
    @Getter
    @ToString
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ToolCalls {

        // TODO

    }
}