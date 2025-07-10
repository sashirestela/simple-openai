package io.github.sashirestela.openai.domain.completion;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.github.sashirestela.openai.common.StreamOptions;
import io.github.sashirestela.slimvalidator.constraints.ObjectType;
import io.github.sashirestela.slimvalidator.constraints.ObjectType.Schema;
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
    @ObjectType(schema = Schema.COLL, baseClass = { String.class, Integer.class })
    @ObjectType(schema = Schema.COLL_COLL, baseClass = Integer.class)
    private Object prompt;

    @Range(min = 0, max = 20)
    private Integer bestOf;

    private Boolean echo;

    @Range(min = -2.0, max = 2.0)
    private Double frequencyPenalty;

    private Map<String, Integer> logitBias;

    @Range(min = 0, max = 5)
    private Integer logprobs;

    private Integer maxTokens;

    @Range(min = 1, max = 128)
    private Integer n;

    @Range(min = -2.0, max = 2.0)
    private Double presencePenalty;

    private Integer seed;

    @ObjectType(baseClass = String.class)
    @ObjectType(schema = Schema.COLL, baseClass = String.class, maxSize = 4)
    private Object stop;

    @With
    private Boolean stream;

    @With
    private StreamOptions streamOptions;

    private String suffix;

    @Range(min = 0.0, max = 2.0)
    private Double temperature;

    @Range(min = 0.0, max = 1.0)
    private Double topP;

    private String user;

}
