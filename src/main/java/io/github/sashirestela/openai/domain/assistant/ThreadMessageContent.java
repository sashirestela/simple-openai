package io.github.sashirestela.openai.domain.assistant;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ImageFileContent.class, name = "image_file"),
        @JsonSubTypes.Type(value = TextContent.class, name = "text")
})
public interface ThreadMessageContent {

}