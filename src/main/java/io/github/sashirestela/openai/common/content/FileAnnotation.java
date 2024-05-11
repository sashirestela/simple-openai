package io.github.sashirestela.openai.common.content;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = FileAnnotation.FileCitationAnnotation.class, name = "file_citation"),
        @JsonSubTypes.Type(value = FileAnnotation.FilePathAnnotation.class, name = "file_path")
})
@Getter
public class FileAnnotation {

    protected Integer index;
    protected AnnotationType type;
    protected String text;
    protected Integer startIndex;
    protected Integer endIndex;

    public enum AnnotationType {

        @JsonProperty("file_citation")
        FILE_CITATION,

        @JsonProperty("file_path")
        FILE_PATH;

    }

    @NoArgsConstructor
    @Getter
    @ToString
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class FileCitationAnnotation extends FileAnnotation {

        private FileCitation fileCitation;

        @NoArgsConstructor
        @Getter
        @ToString
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class FileCitation {

            private String fileId;
            private String quote;

        }

    }

    @NoArgsConstructor
    @Getter
    @ToString
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class FilePathAnnotation extends FileAnnotation {

        private FilePath filePath;

        @NoArgsConstructor
        @Getter
        @ToString
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class FilePath {

            private String fileId;

        }

    }

}
