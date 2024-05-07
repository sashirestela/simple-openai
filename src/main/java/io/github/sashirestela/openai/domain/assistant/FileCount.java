package io.github.sashirestela.openai.domain.assistant;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FileCount {

    private Integer inProgress;
    private Integer completed;
    private Integer failed;
    private Integer cancelled;
    private Integer total;

}
