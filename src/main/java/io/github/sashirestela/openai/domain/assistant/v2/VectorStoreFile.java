package io.github.sashirestela.openai.domain.assistant.v2;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class VectorStoreFile {

    private String id;
    private String object;
    private Integer usageBytes;
    private Integer createdAt;
    private String vectorStoreId;
    private FileStatus status;
    private ThreadRun.LastError lastError;

    public enum FileStatus {

        @JsonProperty("in_progress")
        IN_PROGRESS,

        @JsonProperty("completed")
        COMPLETED,

        @JsonProperty("cancelled")
        CANCELLED,

        @JsonProperty("failed")
        FAILED;

    }

}
