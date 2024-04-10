package io.github.sashirestela.openai.domain.chat.content;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.github.sashirestela.slimvalidator.constraints.Required;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ContentPartImage extends ContentPart {

    @Required
    private ImageUrl imageUrl;

    public ContentPartImage(ImageUrl imageUrl) {
        this.type = ContentPartType.IMAGE_URL;
        this.imageUrl = imageUrl;
    }

}
