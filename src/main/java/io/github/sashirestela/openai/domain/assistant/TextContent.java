package io.github.sashirestela.openai.domain.assistant;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@JsonTypeName("text")
@NoArgsConstructor
@Getter
@ToString
public class TextContent implements ThreadMessageContent {

    private Text text;

    public String getValue() {
        return (text == null) ? null : text.getValue();
    }

    public List<TextContentAnnotation> getAnnotations() {
        return (text == null) ? null : text.getAnnotations();
    }

    @NoArgsConstructor
    @Getter
    @ToString
    public class Text {

        private String value;
        private List<TextContentAnnotation> annotations;

    }

}
