package io.github.sashirestela.openai.domain.batch;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.github.sashirestela.slimvalidator.constraints.Required;
import io.github.sashirestela.slimvalidator.constraints.Size;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
@JsonInclude(Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BatchRequest {

    @Required
    private String inputFileId;

    @Required
    private EndpointType endpoint;

    @Required
    private CompletionWindowType completionWindow;

    @Size(max = 16)
    private Map<String, String> metadata;

    public enum CompletionWindowType {

        @JsonProperty("24h")
        T24H;

    }

}
