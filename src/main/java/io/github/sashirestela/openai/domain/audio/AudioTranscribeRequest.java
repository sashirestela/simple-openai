package io.github.sashirestela.openai.domain.audio;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.github.sashirestela.slimvalidator.constraints.Extension;
import io.github.sashirestela.slimvalidator.constraints.Range;
import io.github.sashirestela.slimvalidator.constraints.Required;
import io.github.sashirestela.slimvalidator.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import lombok.With;

import java.nio.file.Path;
import java.util.Set;

@Getter
@Builder
@JsonInclude(Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AudioTranscribeRequest {

    @Required
    @Extension({ "flac", "mp3", "mp4", "mpeg", "mpga", "m4a", "ogg", "wav", "webm" })
    private Path file;

    @Required
    private String model;

    private String language;

    private String prompt;

    @With
    private AudioRespFmt responseFormat;

    @Range(min = 0.0, max = 1.0)
    private Double temperature;

    @Singular
    @Size(max = 2)
    private Set<TimestampGranularity> timestampGranularities;

}
