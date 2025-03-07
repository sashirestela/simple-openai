package io.github.sashirestela.openai.domain.assistant;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.github.sashirestela.slimvalidator.constraints.ObjectType;
import io.github.sashirestela.slimvalidator.constraints.Range;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RankingOption {

    @ObjectType(baseClass = RankerType.class)
    @ObjectType(baseClass = String.class)
    private Object ranker;

    @Range(min = 0.0, max = 1.0)
    private Double scoreThreshold;

    public enum RankerType {

        @JsonProperty("auto")
        AUTO,

        @JsonProperty("default_2024_08_21")
        DEFAULT_2024_08_21
    }

}
