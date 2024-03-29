package io.github.sashirestela.openai.domain.assistant;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import lombok.With;

import java.util.List;
import java.util.Map;

@Getter
@Builder
@JsonInclude(Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ThreadCreateAndRunRequest {

    private String assistantId;
    private ThreadMessageList thread;
    private String model;
    private String instructions;
    @Singular
    private List<AssistantTool> tools;
    private Map<String, String> metadata;
    @With
    private boolean stream;

}
