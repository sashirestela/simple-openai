package io.github.sashirestela.openai.domain.image;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

import java.nio.file.Path;

@Getter
@SuperBuilder
@JsonInclude(Include.NON_EMPTY)
public class ImageEditsRequest extends AbstractImageRequest {

    @NonNull
    private Path image;
    private Path mask;
    @NonNull
    private String prompt;

}
