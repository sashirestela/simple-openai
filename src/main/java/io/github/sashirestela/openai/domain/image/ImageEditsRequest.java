package io.github.sashirestela.openai.domain.image;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.github.sashirestela.slimvalidator.constraints.Extension;
import io.github.sashirestela.slimvalidator.constraints.Required;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.nio.file.Path;

@Getter
@SuperBuilder
@JsonInclude(Include.NON_EMPTY)
public class ImageEditsRequest extends AbstractImageRequest {

    @Required
    @Extension({ "png" })
    private Path image;

    @Required
    private String prompt;

    @Extension({ "png" })
    private Path mask;

}
