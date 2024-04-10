package io.github.sashirestela.openai.domain.image;

import io.github.sashirestela.slimvalidator.constraints.Extension;
import io.github.sashirestela.slimvalidator.constraints.Required;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.nio.file.Path;

@Getter
@SuperBuilder
public class ImageVariationsRequest extends AbstractImageRequest {

    @Required
    @Extension({ "png" })
    private Path image;

}
