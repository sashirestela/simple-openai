package io.github.sashirestela.openai.domain.image;

import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

import java.nio.file.Path;

@Getter
@SuperBuilder
public class ImageVariationsRequest extends AbstractImageRequest {

    @NonNull
    private Path image;

}
