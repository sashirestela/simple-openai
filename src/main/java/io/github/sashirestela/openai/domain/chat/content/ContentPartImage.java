package io.github.sashirestela.openai.domain.chat.content;

import lombok.Getter;
import lombok.NonNull;

@Getter
public class ContentPartImage extends ContentPart {

    private ImageUrl imageUrl;

    public ContentPartImage(@NonNull ImageUrl imageUrl) {
        this.type = ContentPartType.IMAGE_URL;
        this.imageUrl = imageUrl;
    }
}