package io.github.sashirestela.openai.domain.realtime;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type",
        visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ClientEvent.SessionUpdate.class, name = Realtime.SESSION_UPDATE),
        @JsonSubTypes.Type(value = ClientEvent.InputAudioBufferAppend.class, name = Realtime.INPUT_AUDIO_BUFFER_APPEND),
        @JsonSubTypes.Type(value = ClientEvent.InputAudioBufferCommit.class, name = Realtime.INPUT_AUDIO_BUFFER_COMMIT),
        @JsonSubTypes.Type(value = ClientEvent.InputAudioBufferClear.class, name = Realtime.INPUT_AUDIO_BUFFER_CLEAR),
        @JsonSubTypes.Type(value = ClientEvent.ConversationItemCreate.class, name = Realtime.CONVERSATION_ITEM_CREATE),
        @JsonSubTypes.Type(value = ClientEvent.ConversationItemTruncate.class,
                name = Realtime.CONVERSATION_ITEM_TRUNCATE),
        @JsonSubTypes.Type(value = ClientEvent.ConversationItemDelete.class, name = Realtime.CONVERSATION_ITEM_DELETE),
        @JsonSubTypes.Type(value = ClientEvent.ResponseCreate.class, name = Realtime.RESPONSE_CREATE),
        @JsonSubTypes.Type(value = ClientEvent.ResponseCancel.class, name = Realtime.RESPONSE_CANCEL),
        @JsonSubTypes.Type(value = ServerEvent.ErrorEvent.class, name = Realtime.ERROR),
        @JsonSubTypes.Type(value = ServerEvent.SessionCreated.class, name = Realtime.SESSION_CREATED),
        @JsonSubTypes.Type(value = ServerEvent.SessionUpdated.class, name = Realtime.SESSION_UPDATED),
        @JsonSubTypes.Type(value = ServerEvent.ConversationCreated.class, name = Realtime.CONVERSATION_CREATED),
        @JsonSubTypes.Type(value = ServerEvent.ConversationItemCreated.class,
                name = Realtime.CONVERSATION_ITEM_CREATED),
        @JsonSubTypes.Type(value = ServerEvent.ConversationItemAudioTransCompleted.class,
                name = Realtime.CONVERSATION_ITEM_AUDIO_TRANS_COMPLETED),
        @JsonSubTypes.Type(value = ServerEvent.ConversationItemAudioTransFailed.class,
                name = Realtime.CONVERSATION_ITEM_AUDIO_TRANS_FAILED),
        @JsonSubTypes.Type(value = ServerEvent.ConversationItemTruncated.class,
                name = Realtime.CONVERSATION_ITEM_TRUNCATED),
        @JsonSubTypes.Type(value = ServerEvent.ConversationItemDeleted.class,
                name = Realtime.CONVERSATION_ITEM_DELETED),
        @JsonSubTypes.Type(value = ServerEvent.InputAudioBufferCommitted.class,
                name = Realtime.INPUT_AUDIO_BUFFER_COMMITTED),
        @JsonSubTypes.Type(value = ServerEvent.InputAudioBufferCleared.class,
                name = Realtime.INPUT_AUDIO_BUFFER_CLEARED),
        @JsonSubTypes.Type(value = ServerEvent.InputAudioBufferSpeechStarted.class,
                name = Realtime.INPUT_AUDIO_BUFFER_SPEECH_STARTED),
        @JsonSubTypes.Type(value = ServerEvent.InputAudioBufferSpeechStopped.class,
                name = Realtime.INPUT_AUDIO_BUFFER_SPEECH_STOPPED),
        @JsonSubTypes.Type(value = ServerEvent.ResponseCreated.class, name = Realtime.RESPONSE_CREATED),
        @JsonSubTypes.Type(value = ServerEvent.ResponseDone.class, name = Realtime.RESPONSE_DONE),
        @JsonSubTypes.Type(value = ServerEvent.ResponseOutputItemAdded.class,
                name = Realtime.RESPONSE_OUTPUT_ITEM_ADDED),
        @JsonSubTypes.Type(value = ServerEvent.ResponseOutputItemDone.class, name = Realtime.RESPONSE_OUTPUT_ITEM_DONE),
        @JsonSubTypes.Type(value = ServerEvent.ResponseContentPartAdded.class,
                name = Realtime.RESPONSE_CONTENT_PART_ADDED),
        @JsonSubTypes.Type(value = ServerEvent.ResponseContentPartDone.class,
                name = Realtime.RESPONSE_CONTENT_PART_DONE),
        @JsonSubTypes.Type(value = ServerEvent.ResponseTextDelta.class, name = Realtime.RESPONSE_TEXT_DELTA),
        @JsonSubTypes.Type(value = ServerEvent.ResponseTextDone.class, name = Realtime.RESPONSE_TEXT_DONE),
        @JsonSubTypes.Type(value = ServerEvent.ResponseAudioTranscriptDelta.class,
                name = Realtime.RESPONSE_AUDIO_TRANSCRIPT_DELTA),
        @JsonSubTypes.Type(value = ServerEvent.ResponseAudioTranscriptDone.class,
                name = Realtime.RESPONSE_AUDIO_TRANSCRIPT_DONE),
        @JsonSubTypes.Type(value = ServerEvent.ResponseAudioDelta.class, name = Realtime.RESPONSE_AUDIO_DELTA),
        @JsonSubTypes.Type(value = ServerEvent.ResponseAudioDone.class, name = Realtime.RESPONSE_AUDIO_DONE),
        @JsonSubTypes.Type(value = ServerEvent.ResponseFunctionCallArgumentsDelta.class,
                name = Realtime.RESPONSE_FUNCTION_CALL_ARGS_DELTA),
        @JsonSubTypes.Type(value = ServerEvent.ResponseFunctionCallArgumentsDone.class,
                name = Realtime.RESPONSE_FUNCTION_CALL_ARGS_DONE),
        @JsonSubTypes.Type(value = ServerEvent.RateLimitsUpdated.class, name = Realtime.RATE_LIMITS_UPDATED),
})
@Getter
@ToString
@NoArgsConstructor
@SuperBuilder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public abstract class BaseEvent {

    protected String eventId;
    protected String type;

}
