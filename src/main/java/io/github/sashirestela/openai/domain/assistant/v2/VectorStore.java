package io.github.sashirestela.openai.domain.assistant.v2;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Map;

@NoArgsConstructor
@Getter
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class VectorStore {

    private String id;
    private String object;
    private Integer createdAt;
    private String name;
    private Integer usageBytes;
    private FileCount fileCounts;
    private VectorStoreStatus status;
    private VectorStoreRequest.ExpiresAfter expriresAfter;
    private Integer expiresAt;
    private Integer lastActiveAt;
    private Map<String, String> metadata;

    public enum VectorStoreStatus {

        @JsonProperty("expired")
        EXPIRED,
    
        @JsonProperty("in_progress")
        IN_PROGRESS,
    
        @JsonProperty("completed")
        COMPLETED;
    
    }
    
    @NoArgsConstructor
    @Getter
    @ToString
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class FileCount {

        private Integer inProgress;
        private Integer completed;
        private Integer failed;
        private Integer cancelled;
        private Integer total;

    }

}
