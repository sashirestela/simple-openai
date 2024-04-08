package io.github.sashirestela.openai.domain.assistant;

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
    private ThreadMessageRequest.Role role;

    @Required
    private String content;

    @Singular
    @Size(max = 10)
    private List<String> fileIds;

    @Size(max = 16)
    private Map<String, String> metadata;

    public static enum Role {

        @JsonProperty("user")
        USER,

        @JsonProperty("assistant")
        ASSISTANT;

    }

}
