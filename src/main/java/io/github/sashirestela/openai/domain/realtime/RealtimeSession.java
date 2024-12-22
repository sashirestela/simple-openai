package io.github.sashirestela.openai.domain.realtime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.github.sashirestela.openai.common.function.FunctionDef;
import io.github.sashirestela.openai.common.tool.ToolChoiceOption;
import io.github.sashirestela.openai.common.tool.ToolType;
import io.github.sashirestela.openai.domain.chat.ChatRequest.Modality;
import io.github.sashirestela.slimvalidator.constraints.ObjectType;
import io.github.sashirestela.slimvalidator.constraints.Range;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Singular;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RealtimeSession {

    protected String id;

    protected String object;

    @Singular
    protected List<Modality> modalities;

    protected String model;

    protected String instructions;

    protected VoiceRealtime voice;

    protected AudioFormatRealtime inputAudioFormat;

    protected AudioFormatRealtime outputAudioFormat;

    protected InputAudioTranscription inputAudioTranscription;

    protected TurnDetection turnDetection;

    @Singular
    protected List<ToolRealtime> tools;

    @ObjectType(baseClass = String.class)
    @ObjectType(baseClass = ToolChoiceOption.class)
    protected Object toolChoice;

    @Range(min = 0.6, max = 1.2)
    protected Double temperature;

    @Range(min = 1, max = 4096)
    @ObjectType(baseClass = Integer.class)
    @ObjectType(baseClass = String.class)
    protected Object maxResponseOutputTokens;

    public enum VoiceRealtime {

        @JsonProperty("ash")
        ASH,

        @JsonProperty("ballad")
        BALLAD,

        @JsonProperty("coral")
        CORAL,

        @JsonProperty("sage")
        SAGE,

        @JsonProperty("verse")
        VERSE,

        @JsonProperty("alloy")
        ALLOY,

        @JsonProperty("echo")
        ECHO,

        @JsonProperty("shimmer")
        SHIMMER;

    }

    public enum AudioFormatRealtime {

        @JsonProperty("pcm16")
        PCM16,

        @JsonProperty("g711_ulaw")
        G711_ULAW,

        @JsonProperty("g711_alaw")
        G711_ALAW;

    }

    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class InputAudioTranscription {

        private String model;

        public static InputAudioTranscription of(String model) {
            return new InputAudioTranscription(model);
        }

    }

    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TurnDetection {

        @Builder.Default
        private String type = "server_vad";

        @Range(min = 0.0, max = 1.0)
        private Double threshold;

        private Integer prefixPaddingMs;

        private Integer silenceDurationMs;

        private Boolean createReponse;

    }

    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ToolRealtime {

        private ToolType type;
        private String name;
        private String description;
        private JsonNode parameters;

        public static ToolRealtime of(FunctionDef function) {
            return new ToolRealtime(
                    ToolType.FUNCTION,
                    function.getName(),
                    function.getDescription(),
                    function.getSchemaConverter().convert(function.getFunctionalClass()));
        }

    }

}
