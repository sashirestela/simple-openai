package io.github.sashirestela.openai.domain.chat.content;

import io.github.sashirestela.slimvalidator.constraints.Required;
import lombok.Getter;

@Getter
public class ContentPartText extends ContentPart {

    @Required
    private String text;

    public ContentPartText(String text) {
        this.type = ContentPartType.TEXT;
        this.text = text;
    }

}
