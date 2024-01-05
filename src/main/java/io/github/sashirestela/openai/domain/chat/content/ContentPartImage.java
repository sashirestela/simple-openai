package io.github.sashirestela.openai.domain.chat.content;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NonNull;

@Getter
public class ContentPartImage extends ContentPart {

    @JsonProperty("image_url")
    private ImageUrl imageUrl;

    public ContentPartImage(@NonNull ImageUrl imageUrl) {
        this.type = ContentPartType.IMAGE_URL;
        this.imageUrl = imageUrl;
    }
}