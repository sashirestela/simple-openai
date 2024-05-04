package io.github.sashirestela.openai.domain.assistant;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class LastError {

    private ErrorCode code;
    private String message;

    public enum ErrorCode {

        @JsonProperty("server_error")
        SERVER_ERROR,

        @JsonProperty("rate_limit_exceeded")
        RATE_LIMIT_EXCEEDED,

        @JsonProperty("invalid_prompt")
        INVALID_PROMPT;
    }

}
