package io.github.sashirestela.openai.domain.image;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class ImageRequest extends AbstractImageRequest {

    @NonNull
    private String prompt;

    @JsonInclude(Include.NON_NULL)
    private Quality quality;

    @JsonInclude(Include.NON_NULL)
    private Style style;

}