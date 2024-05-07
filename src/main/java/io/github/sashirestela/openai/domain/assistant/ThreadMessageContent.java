package io.github.sashirestela.openai.domain.assistant;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ThreadMessageContent.ImageFileContent.class, name = "image_file"),
        @JsonSubTypes.Type(value = ThreadMessageContent.TextContent.class, name = "text")
})
@Getter
public class ThreadMessageContent {

    protected Integer index;
    protected ContentType type;

    public enum ContentType {

        @JsonProperty("image_file")
        IMAGE_FILE,

        @JsonProperty("text")
        TEXT;

    }

    @NoArgsConstructor
    @Getter
    @ToString
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ImageFileContent extends ThreadMessageContent {

        private ImageFile imageFile;

        @NoArgsConstructor
        @Getter
        @ToString
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class ImageFile {

            private String fileId;

        }

    }

    @NoArgsConstructor
    @Getter
    @ToString
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class TextContent extends ThreadMessageContent {

        private TextAnnotation text;

        @NoArgsConstructor
        @Getter
        @ToString
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class TextAnnotation {

            private String value;
            private List<FileAnnotation> annotations;

        }

    }

}
