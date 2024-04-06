package io.github.sashirestela.openai.domain.image;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.github.sashirestela.slimvalidator.constraints.Required;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@JsonInclude(Include.NON_EMPTY)
public class ImageRequest extends AbstractImageRequest {

    @Required
    private String prompt;

    private Quality quality;

    private Style style;

}
