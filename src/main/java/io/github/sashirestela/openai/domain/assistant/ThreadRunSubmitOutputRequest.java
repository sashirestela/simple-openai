package io.github.sashirestela.openai.domain.assistant;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.github.sashirestela.slimvalidator.constraints.Required;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import lombok.ToString;
import lombok.With;

import java.util.List;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ThreadRunSubmitOutputRequest {

    @Required
    @Singular
    private List<ToolOutput> toolOutputs;

    @With
    private Boolean stream;

    @Getter
    @Builder
    @ToString
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ToolOutput {

        private String toolCallId;
        private String output;

    }

}
