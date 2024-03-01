package io.github.sashirestela.openai.domain.chat.content;

public abstract class ContentPart {

    protected ContentPartType type;

    public ContentPartType getType() {
        return type;
    }

}
