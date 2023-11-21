package io.github.sashirestela.openai.domain.chat.content;

import lombok.Getter;
import lombok.NonNull;

@Getter
public class ContentPartText extends ContentPart {

    private String text;

    public ContentPartText(@NonNull String text) {
        this.type = ContentPartType.TEXT;
        this.text = text;
    }
}