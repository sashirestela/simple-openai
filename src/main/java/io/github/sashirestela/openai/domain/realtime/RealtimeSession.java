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
import io.github.sashirestela.slimvalidator.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Singular;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Map;

@Getter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RealtimeSession {

    private String id;

    private String object;

    private SecretConfig clientSecret;

    protected AudioFormatRealtime inputAudioFormat;

    private InputAudioNoiseReduction inputAudioNoiseReduction;

    protected InputAudioTranscription inputAudioTranscription;

    protected String instructions;

    @Range(min = 1, max = 4096)
    @ObjectType(baseClass = { Integer.class, String.class })
    protected Object maxResponseOutputTokens;

    @Singular
    protected List<Modality> modalities;

    private String model;

    protected AudioFormatRealtime outputAudioFormat;

    @Range(min = 0.25, max = 1.5)
    protected Double speed;

    @Range(min = 0.6, max = 1.2)
    protected Double temperature;

    @ObjectType(baseClass = { String.class, ToolChoiceOption.class })
    protected Object toolChoice;

    @Singular
    protected List<ToolRealtime> tools;

    @ObjectType(baseClass = { String.class, TracingConfig.class })
    protected Object tracing;

    protected TurnDetection turnDetection;

    protected VoiceRealtime voice;

    public enum VoiceRealtime {

        @JsonProperty("alloy")
        ALLOY,

        @JsonProperty("ash")
        ASH,

        @JsonProperty("ballad")
        BALLAD,

        @JsonProperty("coral")
        CORAL,

        @JsonProperty("echo")
        ECHO,

        @JsonProperty("fable")
        FABLE,

        @JsonProperty("onyx")
        ONYX,

        @JsonProperty("nova")
        NOVA,

        @JsonProperty("sage")
        SAGE,

        @JsonProperty("shimmer")
        SHIMMER,

        @JsonProperty("verse")
        VERSE;

    }

    public enum AudioFormatRealtime {

        @JsonProperty("pcm16")
        PCM16,

        @JsonProperty("g711_ulaw")
        G711_ULAW,

        @JsonProperty("g711_alaw")
        G711_ALAW;

    }

    public enum NoiseReductionType {

        @JsonProperty("near_field")
        NEAR_FIELD,

        @JsonProperty("far_field")
        FAR_FIELD;

    }

    public enum TurnDetectionType {

        @JsonProperty("server_vad")
        SERVER_VAD,

        @JsonProperty("semantic_vad")
        SEMANTIC_VAD;

    }

    public enum EagernessType {

        @JsonProperty("low")
        LOW,

        @JsonProperty("medium")
        MEDIUM,

        @JsonProperty("high")
        HIGH,

        @JsonProperty("auto")
        AUTO;

    }

    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class InputAudioTranscription {

        @Size(min = 2, max = 2)
        private String language;

        private String model;

        private String prompt;

        public static InputAudioTranscription of(String model) {
            return InputAudioTranscription.builder().model(model).build();
        }

    }

    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class TurnDetection {

        private TurnDetectionType type;

        private EagernessType eagerness;

        @Range(min = 0.0, max = 1.0)
        private Double threshold;

        private Integer prefixPaddingMs;

        private Integer silenceDurationMs;

        private Boolean createReponse;

        private Boolean interruptResponse;

    }

    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
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

    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class InputAudioNoiseReduction {

        private NoiseReductionType type;

        public static InputAudioNoiseReduction of(NoiseReductionType type) {
            return new InputAudioNoiseReduction(type);
        }

    }

    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class SecretConfig {

        private Expiration expiresAfter;

        public static SecretConfig of(Integer seconds) {
            return new SecretConfig(new Expiration(seconds));
        }

        @Getter
        @ToString
        public static class Expiration {

            private String anchor;

            @Range(min = 10, max = 7200)
            private Integer seconds;

            public Expiration(Integer seconds) {
                this.anchor = "created_at";
                this.seconds = seconds;
            }

        }

    }

    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TracingConfig {

        private String workflowName;

        private String groupId;

        private Map<String, Object> metadata;

    }

}
