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
import io.github.sashirestela.openai.domain.response.ResponseTool.ContextSize;
import io.github.sashirestela.slimvalidator.constraints.ObjectType;
import io.github.sashirestela.slimvalidator.constraints.ObjectType.Schema;
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
    @ObjectType(schema = Schema.COLL, baseClass = String.class, maxSize = 4)
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
    @ObjectType(baseClass = { ToolChoiceOption.class, ToolChoice.class })
    private Object toolChoice;

    private Boolean parallelToolCalls;

    private String user;

    private WebSearchOptions webSearchOptions;

    public enum Modality {

        @JsonProperty("text")
        TEXT,

        @JsonProperty("audio")
        AUDIO;

    }

    public enum ServiceTier {

        @JsonProperty("auto")
        AUTO,

        @JsonProperty("flex")
        FLEX,

        @JsonProperty("default")
        DEFAULT,

        @JsonProperty("on_demand") // See https://console.groq.com/docs/flex-processing#service-tier-parameter
        ON_DEMAND;
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

    @Getter
    @ToString
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class WebSearchOptions {

        private ContextSize searchContextSize;
        private UserLocation userLocation;

        @Builder
        public WebSearchOptions(ContextSize searchContextSize, UserLocation userLocation) {
            this.searchContextSize = searchContextSize;
            this.userLocation = userLocation;
        }

        public static WebSearchOptions of() {
            return WebSearchOptions.builder().build();
        }

        @Getter
        @ToString
        @JsonInclude(Include.NON_EMPTY)
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class UserLocation {

            private ApproxLocation approximate;
            private String type;

            private UserLocation(ApproxLocation approximate) {
                this.approximate = approximate;
                this.type = "approximate";
            }

            public static UserLocation of(ApproxLocation approximate) {
                return new UserLocation(approximate);
            }

        }

        @Getter
        @ToString
        @JsonInclude(Include.NON_EMPTY)
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class ApproxLocation {

            private String city;
            private String country;
            private String region;
            private String timezone;

            @Builder
            public ApproxLocation(String city, String country, String region, String timezone) {
                this.city = city;
                this.country = country;
                this.region = region;
                this.timezone = timezone;
            }

        }

    }

}
