package io.github.sashirestela.openai.domain.realtime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.github.sashirestela.openai.domain.chat.ChatRequest.Modality;
import io.github.sashirestela.openai.domain.realtime.RealtimeSession.AudioFormatRealtime;
import io.github.sashirestela.openai.domain.realtime.RealtimeSession.InputAudioNoiseReduction;
import io.github.sashirestela.openai.domain.realtime.RealtimeSession.InputAudioTranscription;
import io.github.sashirestela.openai.domain.realtime.RealtimeSession.SecretConfig;
import io.github.sashirestela.openai.domain.realtime.RealtimeSession.TurnDetection;
import lombok.AllArgsConstructor;
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
public class RealtimeTranscriptionSession {

    private String id;

    private String object;

    private SecretConfig clientSecret;

    private List<ItemsToInclude> include;

    protected AudioFormatRealtime inputAudioFormat;

    private InputAudioNoiseReduction inputAudioNoiseReduction;

    protected InputAudioTranscription inputAudioTranscription;

    /**
     * @deprecated Although documented in the API, OpenAI does not yet support this field.
     */
    @Deprecated(forRemoval = false)
    @Singular
    protected List<Modality> modalities;

    protected TurnDetection turnDetection;

    public enum ItemsToInclude {

        @JsonProperty("item.input_audio_transcription.logprobs")
        LOGPROBS;

    }

}
