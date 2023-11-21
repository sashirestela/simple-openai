package io.github.sashirestela.openai.domain.finetuning;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.github.sashirestela.openai.SimpleUncheckedException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
public class HyperParams {

    @JsonInclude(Include.NON_NULL)
    @JsonProperty("batch_size")
    private Object batchSize;

    @JsonInclude(Include.NON_NULL)
    @JsonProperty("learning_rate_multiplier")
    private Object learningRateMultiplier;

    @JsonInclude(Include.NON_NULL)
    @JsonProperty("n_epochs")
    private Object numberEpochs;

    @Builder
    public HyperParams(Object batchSize, Object learningRateMultiplier, Object numberEpochs) {
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
        if (numberEpochs != null && !(numberEpochs instanceof Integer) && !(numberEpochs instanceof String)) {
            throw new SimpleUncheckedException(
                    "The field numberEpochs must be Integer or String classes.",
                    null, null);
        }
        this.batchSize = batchSize;
        this.learningRateMultiplier = learningRateMultiplier;
        this.numberEpochs = numberEpochs;
    }
}