package io.github.sashirestela.openai.domain.assistant;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@NoArgsConstructor
@Getter
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ThreadRunStepDelta {

    private String id;
    private String object;
    private ThreadRunStepDeltaDetail delta;

    @NoArgsConstructor
    @Getter
    @ToString
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ThreadRunStepDeltaDetail {

        private StepDetails stepDetails;

    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
    @JsonSubTypes({
            @JsonSubTypes.Type(value = MessageCreationStepDetail.class, name = "message_creation"),
            @JsonSubTypes.Type(value = ToolCallsStepDetail.class, name = "tool_calls")
    })
    public static interface StepDetails {
    }

    @NoArgsConstructor
    @Getter
    @ToString
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class MessageCreationStepDetail implements StepDetails {

        private MessageCreation messageCreation;

    }

    @NoArgsConstructor
    @Getter
    @ToString
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ToolCallsStepDetail implements StepDetails {

        private List<ThreadRunStep.ToolCall> toolCalls;

    }

    @NoArgsConstructor
    @Getter
    @ToString
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class MessageCreation {

        private String messageId;

    }

}
