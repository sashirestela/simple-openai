package io.github.sashirestela.openai.domain.realtime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.github.sashirestela.openai.common.tool.ToolChoiceOption;
import io.github.sashirestela.openai.domain.chat.ChatRequest.Modality;
import io.github.sashirestela.slimvalidator.constraints.ObjectType;
import io.github.sashirestela.slimvalidator.constraints.Range;
import io.github.sashirestela.slimvalidator.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Singular;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RealtimeResponse {

    @Singular
    private List<Modality> modalities;

    private String instructions;

    private RealtimeSession.VoiceRealtime voice;

    private RealtimeSession.AudioFormatRealtime outputAudioFormat;

    @Singular
    private List<RealtimeSession.ToolRealtime> tools;

    @ObjectType(baseClass = String.class)
    @ObjectType(baseClass = ToolChoiceOption.class)
    private Object toolChoice;

    @Range(min = 0.6, max = 1.2)
    private Double temperature;

    @Range(min = 1, max = 4096)
    @ObjectType(baseClass = Integer.class)
    @ObjectType(baseClass = String.class)
    private Object maxResponseOutputTokens;

    private ConversationType conversation;

    @Size(max = 16)
    private Map<String, String> metadata;

    @Singular("inputItem")
    private List<Item> input;

    public enum ConversationType {
        @JsonProperty("auto")
        AUTO,

        @JsonProperty("none")
        NONE;
    }

}
