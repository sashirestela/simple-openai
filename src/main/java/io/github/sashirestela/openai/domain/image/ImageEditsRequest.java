package io.github.sashirestela.openai.domain.image;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.github.sashirestela.slimvalidator.constraints.Extension;
import io.github.sashirestela.slimvalidator.constraints.ObjectType;
import io.github.sashirestela.slimvalidator.constraints.ObjectType.Schema;
import io.github.sashirestela.slimvalidator.constraints.Required;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.nio.file.Path;

@Getter
@SuperBuilder
@JsonInclude(Include.NON_EMPTY)
public class ImageEditsRequest extends AbstractImageRequest {

    @Required
    @ObjectType(baseClass = Path.class)
    @ObjectType(schema = Schema.COLL, baseClass = Path.class)
    private Object image;

    @Required
    private String prompt;

    private Background background;

    @Extension({ "png" })
    private Path mask;

    private Quality quality;

}
