package io.github.sashirestela.openai.domain.audio;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.github.sashirestela.slimvalidator.constraints.Range;
import io.github.sashirestela.slimvalidator.constraints.Required;
import io.github.sashirestela.slimvalidator.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AudioSpeechRequest {

    @Required
    private String model;

    @Required
    @Size(max = 4096)
    private String input;

    @Required
    private Voice voice;

    private SpeechRespFmt responseFormat;

    @Range(min = 0.25, max = 4.0)
    private Double speed;

}
