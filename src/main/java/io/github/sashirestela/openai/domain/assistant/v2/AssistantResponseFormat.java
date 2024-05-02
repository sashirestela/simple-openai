package io.github.sashirestela.openai.domain.assistant.v2;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@JsonInclude(Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AssistantResponseFormat {

    public static final AssistantResponseFormat TEXT = new AssistantResponseFormat(ResponseFormat.TEXT);
    public static final AssistantResponseFormat JSON_OBJECT = new AssistantResponseFormat(ResponseFormat.JSON_OBJECT);

    private ResponseFormat type;

    public enum ResponseFormat {

        @JsonProperty("text")
        TEXT,

        @JsonProperty("json_object")
        JSON_OBJECT;

    }

}
