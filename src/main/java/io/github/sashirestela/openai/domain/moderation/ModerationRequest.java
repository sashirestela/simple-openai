package io.github.sashirestela.openai.domain.moderation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.github.sashirestela.slimvalidator.constraints.Required;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@JsonInclude(Include.NON_EMPTY)
public class ModerationRequest {

    @Required
    private List<String> input;

    private String model;

}
