package io.github.sashirestela.openai.domain.assistant;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.github.sashirestela.slimvalidator.constraints.Range;
import io.github.sashirestela.slimvalidator.constraints.Required;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@JsonInclude(Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ChunkingStrategy {

    private ChunkingStrategyType type;

    @JsonProperty("static")
    private StaticChunking staticChunking;

    private ChunkingStrategy(ChunkingStrategyType type, StaticChunking staticChunking) {
        this.type = type;
        this.staticChunking = staticChunking;
    }

    public static ChunkingStrategy autoType() {
        return new ChunkingStrategy(ChunkingStrategyType.AUTO, null);
    }

    public static ChunkingStrategy staticType(StaticChunking staticChunking) {
        return new ChunkingStrategy(ChunkingStrategyType.STATIC, staticChunking);
    }

    public enum ChunkingStrategyType {

        @JsonProperty("auto")
        AUTO,

        @JsonProperty("static")
        STATIC,

        @JsonProperty("other")
        OTHER;
    }

    @Getter
    @Builder
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class StaticChunking {

        @Required
        @Range(min = 100, max = 4_096)
        private Integer maxChunkSizeTokens;

        @Required
        @Range(max = 2_048)
        private Integer chunkOverlapTokens;

    }

}
