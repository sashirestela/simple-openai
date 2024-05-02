package io.github.sashirestela.openai.domain.assistant.v2;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
@Getter
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ThreadMessage {

    private String id;
    private String object;
    private Integer createdAt;
    private String threadId;
    private ThreadMessageStatus status;
    private IncompleteDetail incompleteDetails;
    private Integer completedAt;
    private Integer incompleteAt;
    private ThreadMessageRequest.ThreadMessageRole role;
    private List<ThreadMessageContent> content;
    private String assistantId;
    private String runId;
    private List<Attachment> attachments;
    private Map<String, String> metadata;

    public enum ThreadMessageStatus {

        @JsonProperty("in_progress")
        IN_PROGRESS,

        @JsonProperty("incomplete")
        INCOMPLETE,

        @JsonProperty("completed")
        COMPLETED;

    }

    @NoArgsConstructor
    @Getter
    @ToString
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class IncompleteDetail {

        private IncompleteDetailReason reason;

    }

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
