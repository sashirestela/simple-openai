package io.github.sashirestela.openai.domain.assistant;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.github.sashirestela.cleverclient.util.UnixTimestampDeserializer;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
        private List<ToolCall> toolCalls;

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
    public static class ToolCall {

        private String id;
        private String type;
        private CodeInterpreter codeInterpreter;
        private Map<?, ?> retrieval;
        private Function function;

    }

    @NoArgsConstructor
    @Getter
    @ToString
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class CodeInterpreter {

        private String input;
        private List<CodeInterpreterOutput> outputs;

    }

    @NoArgsConstructor
    @Getter
    @ToString
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class CodeInterpreterOutput {

        private String type;
        private String logs;
        private Image image;

    }

    @NoArgsConstructor
    @Getter
    @ToString
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Image {

        private String fileId;

    }

    @NoArgsConstructor
    @Getter
    @ToString
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Function {

        private String name;
        private String arguments;
        private String output;

    }
}