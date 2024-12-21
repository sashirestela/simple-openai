package io.github.sashirestela.openai.domain.finetuning;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.github.sashirestela.slimvalidator.constraints.ObjectType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@JsonInclude(Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class HyperParams {

    @ObjectType(baseClass = Integer.class)
    @ObjectType(baseClass = String.class)
    private Object beta;

    @ObjectType(baseClass = Integer.class)
    @ObjectType(baseClass = String.class)
    private Object batchSize;

    @ObjectType(baseClass = Double.class)
    @ObjectType(baseClass = String.class)
    private Object learningRateMultiplier;

    @ObjectType(baseClass = Integer.class)
    @ObjectType(baseClass = String.class)
    private Object nEpochs;

    public Object getnEpochs() {
        return nEpochs;
    }

}
