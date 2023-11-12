package io.github.sashirestela.openai.domain.assistant;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = FileCitationAnnotation.class, name = "file_citation"),
        @JsonSubTypes.Type(value = FilePathAnnotation.class, name = "file_path")
})
public interface TextContentAnnotation {

}