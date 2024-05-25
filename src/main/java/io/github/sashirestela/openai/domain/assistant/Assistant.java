package io.github.sashirestela.openai.domain.assistant;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.github.sashirestela.openai.common.tool.Tool;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
@Getter
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Assistant {

    private String id;
    private String object;
    private Integer createdAt;
    private String name;
    private String description;
    private String model;
    private String instructions;
    private List<Tool> tools;
    private ToolResource toolResources;
    private Map<String, String> metadata;
    private Double temperature;
    private Double topP;
    private Object responseFormat;

}
