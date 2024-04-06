package io.github.sashirestela.openai.domain.file;

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

}
