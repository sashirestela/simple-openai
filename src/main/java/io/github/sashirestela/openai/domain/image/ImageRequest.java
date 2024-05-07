package io.github.sashirestela.openai.domain.image;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    public enum Quality {

        @JsonProperty("standard")
        STANDARD,

        @JsonProperty("hd")
        HD;

    }

    public enum Style {

        @JsonProperty("vivid")
        VIVID,

        @JsonProperty("natural")
        NATURAL;

    }

}
