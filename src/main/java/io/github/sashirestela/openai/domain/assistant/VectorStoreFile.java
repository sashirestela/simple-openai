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
public class VectorStoreFile {

    private String id;
    private String object;
    private Integer usageBytes;
    private Integer createdAt;
    private String vectorStoreId;
    private FileStatus status;
    private LastError lastError;
    private ChunkingStrategy chunkingStrategy;

}
