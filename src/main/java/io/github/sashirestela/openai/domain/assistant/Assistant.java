package io.github.sashirestela.openai.domain.assistant;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.github.sashirestela.cleverclient.util.UnixTimestampDeserializer;
import io.github.sashirestela.openai.domain.assistant.AssistantRequest.AssistantRequestBuilder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

/**
 * Represents an assistant that can call the model and use tools.
 */
@NoArgsConstructor
@Getter
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Assistant {

    private String id;
    private String object;
    @JsonDeserialize(using = UnixTimestampDeserializer.class)
    private ZonedDateTime createdAt;
    private String name;
    private String description;
    private String model;
    private String instructions;
    private List<AssistantTool> tools;
    private List<String> fileIds;
    private Map<String, String> metadata;

    public AssistantRequestBuilder mutate() {
        return AssistantRequest.builder()
                .model(model)
                .name(name)
                .description(description)
                .instructions(instructions)
                .tools(tools)
                .fileIds(fileIds)
                .metadata(metadata);
    }

}
