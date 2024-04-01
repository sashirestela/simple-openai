package io.github.sashirestela.openai.domain.completion;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.github.sashirestela.slimvalidator.constraints.ObjectType;
import io.github.sashirestela.slimvalidator.constraints.Required;
import lombok.Builder;
import lombok.Getter;
import lombok.With;

import java.util.Map;

@Getter
@Builder
@JsonInclude(Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CompletionRequest {

    @Required
    private String model;

    @Required
    @ObjectType(baseClass = String.class)
    @ObjectType(baseClass = String.class, firstGroup = true)
    @ObjectType(baseClass = Integer.class, firstGroup = true)
    @ObjectType(baseClass = Integer.class, firstGroup = true, secondGroup = true)
    private Object prompt;

    private String suffix;

    private Integer maxTokens;

    private Double temperature;

    private Double topP;

    private Integer n;

    @With
    private Boolean stream;

    private Integer logprobs;

    private Boolean echo;

    @ObjectType(baseClass = String.class)
    @ObjectType(baseClass = String.class, firstGroup = true, maxSize = 4)
    private Object stop;

    private Integer seed;

    private Double presencePenalty;

    private Double frequencyPenalty;

    private Integer bestOf;

    private Map<String, Integer> logitBias;

    private String user;

}
