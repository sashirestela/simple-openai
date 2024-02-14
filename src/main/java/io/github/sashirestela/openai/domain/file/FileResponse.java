package io.github.sashirestela.openai.domain.file;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FileResponse {

    private String id;
    private String object;
    private String purpose;
    private String filename;
    private Integer bytes;
    private Long createdAt;
    @Deprecated private String status;
    @Deprecated private String statusDetails;

}