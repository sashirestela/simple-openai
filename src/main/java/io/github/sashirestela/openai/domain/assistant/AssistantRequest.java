package io.github.sashirestela.openai.domain.assistant;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.github.sashirestela.slimvalidator.constraints.Required;
import io.github.sashirestela.slimvalidator.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.util.List;
import java.util.Map;

@Getter
@Builder
@JsonInclude(Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AssistantRequest {

    @Required
    private String model;

    @Size(max = 256)
    private String name;

    @Size(max = 512)
    private String description;

    @Size(max = 32768)
    private String instructions;

    @Singular
    @Size(max = 128)
    private List<AssistantTool> tools;

    @Singular
    @Size(max = 20)
    private List<String> fileIds;

    @Size(max = 16)
    private Map<String, String> metadata;

    // Required to avoid Javadoc error.
    public static class AssistantRequestBuilder {
    }

}
