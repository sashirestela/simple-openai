package io.github.sashirestela.openai.domain.image;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.sashirestela.slimvalidator.constraints.Range;
import io.github.sashirestela.slimvalidator.constraints.Required;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@JsonInclude(Include.NON_EMPTY)
public class ImageRequest extends AbstractImageRequest {

    @Required
    private String prompt;

    private Background background;

    private Moderation moderation;

    @Range(min = 0, max = 100)
    private Integer outputCompression;

    private OutputFormat outputFormat;

    private Quality quality;

    private Style style;

    public enum Style {

        @JsonProperty("vivid")
        VIVID,

        @JsonProperty("natural")
        NATURAL;

    }

    public enum Moderation {

        @JsonProperty("low")
        LOW,

        @JsonProperty("auto")
        AUTO;

    }

    public enum OutputFormat {

        @JsonProperty("png")
        PNG,

        @JsonProperty("webp")
        WEBP,

        @JsonProperty("jpeg")
        JPEG;

    }

}
