package io.github.sashirestela.openai.domain.assistant;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.github.sashirestela.openai.domain.Usage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Map;

@NoArgsConstructor
@Getter
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ThreadRunStep {

    private String id;
    private String object;
    private Integer createdAt;
    private String assistantId;
    private String threadId;
    private String runId;
    private RunStepType type;
    private RunStepStatus status;
    private StepDetail stepDetails;
    private LastError lastError;
    private Integer expiredAt;
    private Integer cancelledAt;
    private Integer failedAt;
    private Integer completedAt;
    private Map<String, String> metadata;
    private Usage usage;

    public enum RunStepStatus {

        @JsonProperty("in_progress")
        IN_PROGRESS,

        @JsonProperty("cancelled")
        CANCELLED,

        @JsonProperty("failed")
        FAILED,

        @JsonProperty("completed")
        COMPLETED,

        @JsonProperty("expired")
        EXPIRED;

    }

}
