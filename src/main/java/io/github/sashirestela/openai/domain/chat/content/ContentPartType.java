package io.github.sashirestela.openai.domain.chat.content;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ContentPartType {

    @JsonProperty("text")
    TEXT,

    @JsonProperty("image_url")
    IMAGE_URL;

}