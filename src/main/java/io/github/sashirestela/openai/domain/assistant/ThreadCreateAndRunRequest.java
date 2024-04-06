package io.github.sashirestela.openai.domain.assistant;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.github.sashirestela.slimvalidator.constraints.Range;
import io.github.sashirestela.slimvalidator.constraints.Required;
import io.github.sashirestela.slimvalidator.constraints.Size;
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

    @Required
    private String assistantId;

    private ThreadMessageList thread;

    private String model;

    private String instructions;

    @Singular
    @Size(max = 20)
    private List<AssistantTool> tools;

    @Size(max = 16)
    private Map<String, String> metadata;

    @Range(min = 0.0, max = 2.0)
    private Double temperature;

    @With
    private boolean stream;

}
