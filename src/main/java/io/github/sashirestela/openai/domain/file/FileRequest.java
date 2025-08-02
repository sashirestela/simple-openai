package io.github.sashirestela.openai.domain.file;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.sashirestela.slimvalidator.constraints.Required;
import lombok.Builder;
import lombok.Getter;

import java.nio.file.Path;

@Getter
@Builder
public class FileRequest {

    @Required
    private Path file;

    @Required
    private PurposeType purpose;

    public enum PurposeType {

        @JsonProperty("fine-tune")
        FINE_TUNE,

        @JsonProperty("assistants")
        ASSISTANTS,

        @JsonProperty("batch")
        BATCH,

        @JsonProperty("vision")
        USER_DATA,

        @JsonProperty("user_data")
        VISION;

    }

}
