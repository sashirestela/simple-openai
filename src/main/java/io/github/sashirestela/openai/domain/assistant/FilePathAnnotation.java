package io.github.sashirestela.openai.domain.assistant;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@JsonTypeName("file_path")
@NoArgsConstructor
@Getter
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FilePathAnnotation implements TextContentAnnotation {

    private Integer index;
    private String text;
    private FilePath filePath;
    private int startIndex;
    private int endIndex;

}
