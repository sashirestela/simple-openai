package io.github.sashirestela.openai.domain.image;

import java.nio.file.Path;

import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class ImageVariationsRequest extends AbstractImageRequest {

    @NonNull
    private Path image;

}