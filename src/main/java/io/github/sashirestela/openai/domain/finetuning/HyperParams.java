package io.github.sashirestela.openai.domain.finetuning;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.github.sashirestela.openai.SimpleUncheckedException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
@JsonInclude(Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class HyperParams {

    private Object batchSize;
    private Object learningRateMultiplier;
    private Object nEpochs;

    @Builder
    public HyperParams(Object batchSize, Object learningRateMultiplier, Object nEpochs) {
        if (batchSize != null && !(batchSize instanceof Integer) && !(batchSize instanceof String)) {
            throw new SimpleUncheckedException(
                    "The field batchSize must be Integer or String classes.",
                    null, null);
        }
        if (learningRateMultiplier != null && !(learningRateMultiplier instanceof Double)
                && !(learningRateMultiplier instanceof String)) {
            throw new SimpleUncheckedException(
                    "The field learningRateMultiplier must be Double or String classes.",
                    null, null);
        }
        if (nEpochs != null && !(nEpochs instanceof Integer) && !(nEpochs instanceof String)) {
            throw new SimpleUncheckedException(
                    "The field nEpochs must be Integer or String classes.",
                    null, null);
        }
        this.batchSize = batchSize;
        this.learningRateMultiplier = learningRateMultiplier;
        this.nEpochs = nEpochs;
    }
}