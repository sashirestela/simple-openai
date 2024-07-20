package io.github.sashirestela.openai.domain.upload;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.github.sashirestela.openai.domain.file.FileResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Upload {

    private String id;
    private Long createdAt;
    private String filename;
    private Long bytes;
    private String purpose;
    private UploadStatus status;
    private Long expiresAt;
    private String object;
    private FileResponse file;

    public enum UploadStatus {

        @JsonProperty("pending")
        PENDING,

        @JsonProperty("completed")
        COMPLETED,

        @JsonProperty("cancelled")
        CANCELLED,

        @JsonProperty("expired")
        EXPIRED;

    }

}
