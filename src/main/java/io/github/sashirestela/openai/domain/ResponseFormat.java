package io.github.sashirestela.openai.domain;

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
public class ResponseFormat {

    public static final ResponseFormat TEXT = new ResponseFormat(ResponseFormatType.TEXT);
    public static final ResponseFormat JSON_OBJECT = new ResponseFormat(ResponseFormatType.JSON_OBJECT);

    private ResponseFormatType type;

    public enum ResponseFormatType {

        @JsonProperty("text")
        TEXT,

        @JsonProperty("json_object")
        JSON_OBJECT;

    }

}
