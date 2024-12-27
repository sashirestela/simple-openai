package io.github.sashirestela.openai.domain.chat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.github.sashirestela.openai.common.ResponseFormat;
import io.github.sashirestela.openai.common.StreamOptions;
import io.github.sashirestela.openai.common.audio.AudioFormat;
import io.github.sashirestela.openai.common.audio.Voice;
import io.github.sashirestela.openai.common.tool.Tool;
import io.github.sashirestela.openai.common.tool.ToolChoice;
import io.github.sashirestela.openai.common.tool.ToolChoiceOption;
import io.github.sashirestela.slimvalidator.constraints.ObjectType;
import io.github.sashirestela.slimvalidator.constraints.Range;
import io.github.sashirestela.slimvalidator.constraints.Required;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import lombok.ToString;
import lombok.With;

import java.util.List;
import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
@JsonInclude(Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ChatRequest {

    @Required
    @Singular
    private List<ChatMessage> messages;

    @Required
    private String model;

    private Boolean store;

    private ReasoningEffort reasoningEffort;

    private Map<String, String> metadata;

    @Range(min = -2.0, max = 2.0)
    private Double frequencyPenalty;

    private Map<String, Integer> logitBias;

    private Boolean logprobs;

    @Range(min = 0, max = 20)
    private Integer topLogprobs;

    /**
     * @deprecated OpenAI has deperecated this field in favor of max_completion_tokens.
     */
    @Deprecated(since = "3.9.0", forRemoval = true)
    private Integer maxTokens;

    private Integer maxCompletionTokens;

    @Range(min = 1, max = 128)
    private Integer n;

    @Singular
    private List<Modality> modalities;

    private Audio audio;

    @Range(min = -2.0, max = 2.0)
    private Double presencePenalty;

    private ResponseFormat responseFormat;

    private Integer seed;

    private ServiceTier serviceTier;

    @ObjectType(baseClass = String.class)
    @ObjectType(baseClass = String.class, firstGroup = true, maxSize = 4)
    private Object stop;

    @With
    private Boolean stream;

    @With
    private StreamOptions streamOptions;

    @Range(min = 0.0, max = 2.0)
    private Double temperature;

    @Range(min = 0.0, max = 1.0)
    private Double topP;

    @Singular
    private List<Tool> tools;

    @With
    @ObjectType(baseClass = ToolChoiceOption.class)
    @ObjectType(baseClass = ToolChoice.class)
    private Object toolChoice;

    private Boolean parallelToolCalls;

    private String user;

    public enum Modality {

        @JsonProperty("text")
        TEXT,

        @JsonProperty("audio")
        AUDIO;

    }

    public enum ServiceTier {

        @JsonProperty("auto")
        AUTO,

        @JsonProperty("default")
        DEFAULT;

    }

    @Getter
    @ToString
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Audio {

        @Required
        private Voice voice;

        @Required
        private AudioFormat format;

        private Audio(Voice voice, AudioFormat format) {
            this.voice = voice;
            this.format = format;
        }

        public static Audio of(Voice voice, AudioFormat format) {
            return new Audio(voice, format);
        }

    }

    public enum ReasoningEffort {
        @JsonProperty("low")
        LOW,

        @JsonProperty("medium")
        MEDIUM,

        @JsonProperty("high")
        HIGH;
    }

}
