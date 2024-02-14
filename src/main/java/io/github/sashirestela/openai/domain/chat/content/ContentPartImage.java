package io.github.sashirestela.openai.domain.chat.content;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NonNull;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ContentPartImage extends ContentPart {

    private ImageUrl imageUrl;

    public ContentPartImage(@NonNull ImageUrl imageUrl) {
        this.type = ContentPartType.IMAGE_URL;
        this.imageUrl = imageUrl;
    }
}