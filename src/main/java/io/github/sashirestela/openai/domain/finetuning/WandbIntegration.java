package io.github.sashirestela.openai.domain.finetuning;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.github.sashirestela.slimvalidator.constraints.Required;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Singular;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@JsonInclude(Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class WandbIntegration {

    @Required
    private String project;

    private String name;

    private String entity;

    @Singular
    private List<String> tags;

}
