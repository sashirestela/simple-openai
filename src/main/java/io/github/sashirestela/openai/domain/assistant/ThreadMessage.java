package io.github.sashirestela.openai.domain.assistant;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.github.sashirestela.openai.common.content.ContentPart;
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
    private ThreadMessageRole role;
    private List<ContentPart> content;
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

}
