package io.github.sashirestela.openai.domain.realtime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

public abstract class ClientEvent {

    private ClientEvent() {
    }

    @Getter
    @ToString(callSuper = true)
    @NoArgsConstructor
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class SessionUpdate extends BaseEvent {

        private Configuration session;

        private SessionUpdate(String eventId, Configuration session) {
            this.type = Realtime.SESSION_UPDATE;
            this.eventId = eventId;
            this.session = session;
        }

        public static SessionUpdate of(Configuration session) {
            return new SessionUpdate(null, session);
        }

        public static SessionUpdate of(String eventId, Configuration session) {
            return new SessionUpdate(eventId, session);
        }

    }

    @Getter
    @ToString(callSuper = true)
    @NoArgsConstructor
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class InputAudioBufferAppend extends BaseEvent {

        private String audio;

        private InputAudioBufferAppend(String eventId, String audio) {
            this.type = Realtime.INPUT_AUDIO_BUFFER_APPEND;
            this.eventId = eventId;
            this.audio = audio;
        }

        public static InputAudioBufferAppend of(String audio) {
            return new InputAudioBufferAppend(null, audio);
        }

        public static InputAudioBufferAppend of(String eventId, String audio) {
            return new InputAudioBufferAppend(eventId, audio);
        }

    }

    @Getter
    @ToString(callSuper = true)
    @NoArgsConstructor
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class InputAudioBufferCommit extends BaseEvent {

        private InputAudioBufferCommit(String eventId) {
            this.type = Realtime.INPUT_AUDIO_BUFFER_COMMIT;
            this.eventId = eventId;
        }

        public static InputAudioBufferCommit of() {
            return new InputAudioBufferCommit(null);
        }

        public static InputAudioBufferCommit of(String eventId) {
            return new InputAudioBufferCommit(eventId);
        }

    }

    @Getter
    @ToString(callSuper = true)
    @NoArgsConstructor
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class InputAudioBufferClear extends BaseEvent {

        private InputAudioBufferClear(String eventId) {
            this.type = Realtime.INPUT_AUDIO_BUFFER_CLEAR;
            this.eventId = eventId;
        }

        public static InputAudioBufferClear of() {
            return new InputAudioBufferClear(null);
        }

        public static InputAudioBufferClear of(String eventId) {
            return new InputAudioBufferClear(eventId);
        }

    }

    @Getter
    @ToString(callSuper = true)
    @NoArgsConstructor
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ConversationItemCreate extends BaseEvent {

        private String previousItemId;
        private Item item;

        private ConversationItemCreate(String eventId, String previousItemId, Item item) {
            this.type = Realtime.CONVERSATION_ITEM_CREATE;
            this.eventId = eventId;
            this.previousItemId = previousItemId;
            this.item = item;
        }

        public static ConversationItemCreate of(Item item) {
            return new ConversationItemCreate(null, null, item);
        }

        public static ConversationItemCreate of(String previousItemId, Item item) {
            return new ConversationItemCreate(null, previousItemId, item);
        }

        public static ConversationItemCreate of(String eventId, String previousItemId, Item item) {
            return new ConversationItemCreate(eventId, previousItemId, item);
        }

    }

    @Getter
    @ToString(callSuper = true)
    @NoArgsConstructor
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ConversationItemTruncate extends BaseEvent {

        private String itemId;
        private Integer contentIndex;
        private Integer audioEndMs;

        private ConversationItemTruncate(String eventId, String itemId, Integer contentIndex, Integer audioEndMs) {
            this.type = Realtime.CONVERSATION_ITEM_TRUNCATE;
            this.eventId = eventId;
            this.itemId = itemId;
            this.contentIndex = contentIndex;
            this.audioEndMs = audioEndMs;
        }

        public static ConversationItemTruncate of(String itemId, Integer contentIndex, Integer audioEndMs) {
            return new ConversationItemTruncate(null, itemId, contentIndex, audioEndMs);
        }

        public static ConversationItemTruncate of(String eventId, String itemId, Integer contentIndex,
                Integer audioEndMs) {
            return new ConversationItemTruncate(eventId, itemId, contentIndex, audioEndMs);
        }

    }

    @Getter
    @ToString(callSuper = true)
    @NoArgsConstructor
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ConversationItemDelete extends BaseEvent {

        private String itemId;

        private ConversationItemDelete(String eventId, String itemId) {
            this.type = Realtime.CONVERSATION_ITEM_DELETE;
            this.eventId = eventId;
            this.itemId = itemId;
        }

        public static ConversationItemDelete of(String itemId) {
            return new ConversationItemDelete(null, itemId);
        }

        public static ConversationItemDelete of(String eventId, String itemId) {
            return new ConversationItemDelete(eventId, itemId);
        }

    }

    @Getter
    @ToString(callSuper = true)
    @NoArgsConstructor
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ResponseCreate extends BaseEvent {

        private Configuration response;

        private ResponseCreate(String eventId, Configuration response) {
            this.type = Realtime.RESPONSE_CREATE;
            this.eventId = eventId;
            this.response = response;
        }

        public static ResponseCreate of(Configuration response) {
            return new ResponseCreate(null, response);
        }

        public static ResponseCreate of(String eventId, Configuration response) {
            return new ResponseCreate(eventId, response);
        }

    }

    @Getter
    @ToString(callSuper = true)
    @NoArgsConstructor
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ResponseCancel extends BaseEvent {

        private ResponseCancel(String eventId) {
            this.type = Realtime.RESPONSE_CANCEL;
            this.eventId = eventId;
        }

        public static ResponseCancel of() {
            return new ResponseCancel(null);
        }

        public static ResponseCancel of(String eventId) {
            return new ResponseCancel(eventId);
        }

    }

}
