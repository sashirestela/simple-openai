package io.github.sashirestela.openai.domain.completion;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.github.sashirestela.slimvalidator.constraints.ObjectType;
import io.github.sashirestela.slimvalidator.constraints.Range;
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

    @Range(min = 0.0, max = 2.0)
    private Double temperature;

    @Range(min = 0.0, max = 1.0)
    private Double topP;

    @Range(min = 1, max = 128)
    private Integer n;

    @With
    private Boolean stream;

    @Range(min = 0, max = 5)
    private Integer logprobs;

    private Boolean echo;

    @ObjectType(baseClass = String.class)
    @ObjectType(baseClass = String.class, firstGroup = true, maxSize = 4)
    private Object stop;

    private Integer seed;

    @Range(min = -2.0, max = 2.0)
    private Double presencePenalty;

    @Range(min = -2.0, max = 2.0)
    private Double frequencyPenalty;

    @Range(min = 0, max = 20)
    private Integer bestOf;

    private Map<String, Integer> logitBias;

    private String user;

}
