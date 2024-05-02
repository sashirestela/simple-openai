package io.github.sashirestela.openai.domain.assistant.v2;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Map;

@NoArgsConstructor
@Getter
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Thread {

    private String id;
    private String object;
    private Integer createdAt;
    private ToolResource toolResources;
    private Map<String, String> metadata;

}
