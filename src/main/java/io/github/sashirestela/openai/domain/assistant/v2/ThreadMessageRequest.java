package io.github.sashirestela.openai.domain.assistant.v2;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ThreadMessageRequest {

    @Required
    private ThreadMessageRole role;

    @Required
    @Size(max = 256_000)
    private String content;

    @Singular
    private List<Attachment> attachments;

    @Size(max = 16)
    private Map<String, String> metadata;

    public enum ThreadMessageRole {

        @JsonProperty("user")
        USER,

        @JsonProperty("assistant")
        ASSISTANT;

    }

}
