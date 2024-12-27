package io.github.sashirestela.openai.domain.finetuning;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.github.sashirestela.slimvalidator.constraints.Required;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@JsonInclude(Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FineTuningRequest {

    @Required
    private String model;

    @Required
    private String trainingFile;

    private String validationFile;

    /**
     * @deprecated OpenAI has deperecated this field in favor of method, and should be passed in under
     *             the method parameter.
     */
    @Deprecated(since = "3.12.0", forRemoval = true)
    private HyperParams hyperparameters;

    private String suffix;

    @Singular
    private List<Integration> integrations;

    private Integer seed;

    private MethodFineTunning method;

}
