package io.github.sashirestela.openai.domain.realtime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;

public abstract class ServerEvent {

    private ServerEvent() {
    }

    @Getter
    @ToString(callSuper = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ErrorEvent extends BaseEvent {

        private ErrorDetail error;

    }

    @Getter
    @ToString(callSuper = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class SessionCreated extends BaseEvent {

        private Configuration session;

    }

    @Getter
    @ToString(callSuper = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class SessionUpdated extends BaseEvent {

        private Configuration session;

    }

    @Getter
    @ToString(callSuper = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ConversationCreated extends BaseEvent {

        private Conversation conversation;

    }

    @Getter
    @ToString(callSuper = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ConversationItemCreated extends BaseEvent {

        private String previousItemId;
        private Item item;

    }

    @Getter
    @ToString(callSuper = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ConversationItemAudioTransCompleted extends BaseEvent {

        private String itemId;
        private Integer contentIndex;
        private String transcript;

    }

    @Getter
    @ToString(callSuper = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ConversationItemAudioTransFailed extends BaseEvent {

        private String itemId;
        private Integer contentIndex;
        private ErrorDetail error;

    }

    @Getter
    @ToString(callSuper = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ConversationItemTruncated extends BaseEvent {

        private String itemId;
        private Integer contentIndex;
        private Integer audioEndMs;

    }

    @Getter
    @ToString(callSuper = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ConversationItemDeleted extends BaseEvent {

        private String itemId;

    }

    @Getter
    @ToString(callSuper = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class InputAudioBufferCommitted extends BaseEvent {

        private String previousItemId;
        private String itemId;

    }

    @Getter
    @ToString(callSuper = true)
    @NoArgsConstructor
    @SuperBuilder
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class InputAudioBufferCleared extends BaseEvent {
    }

    @Getter
    @ToString(callSuper = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class InputAudioBufferSpeechStarted extends BaseEvent {

        private Integer audioStartMs;
        private String itemId;

    }

    @Getter
    @ToString(callSuper = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class InputAudioBufferSpeechStopped extends BaseEvent {

        private Integer audioEndMs;
        private String itemId;

    }

    @Getter
    @ToString(callSuper = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ResponseCreated extends BaseEvent {

        private Response response;

    }

    @Getter
    @ToString(callSuper = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ResponseDone extends BaseEvent {

        private Response response;

    }

    @Getter
    @ToString(callSuper = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ResponseOutputItemAdded extends BaseEvent {

        private String responseId;
        private Integer outputIndex;
        private Item item;

    }

    @Getter
    @ToString(callSuper = true)
    @NoArgsConstructor
    @SuperBuilder
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ResponseOutputItemDone extends ResponseOutputItemAdded {
    }

    @Getter
    @ToString(callSuper = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public abstract static class BaseResponse extends BaseEvent {

        private String responseId;
        private String itemId;
        private Integer outputIndex;
        private Integer contentIndex;

    }

    @Getter
    @ToString(callSuper = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ResponseContentPartAdded extends BaseResponse {

        private Part part;

    }

    @Getter
    @ToString(callSuper = true)
    @NoArgsConstructor
    @SuperBuilder
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ResponseContentPartDone extends ResponseContentPartAdded {
    }

    @Getter
    @ToString(callSuper = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ResponseTextDelta extends BaseResponse {

        private String delta;

    }

    @Getter
    @ToString(callSuper = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ResponseTextDone extends BaseResponse {

        private String text;

    }

    @Getter
    @ToString(callSuper = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ResponseAudioTranscriptDelta extends BaseResponse {

        private String delta;

    }

    @Getter
    @ToString(callSuper = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ResponseAudioTranscriptDone extends BaseResponse {

        private String transcript;

    }

    @Getter
    @ToString(callSuper = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ResponseAudioDelta extends BaseResponse {

        private String delta;

    }

    @Getter
    @ToString(callSuper = true)
    @NoArgsConstructor
    @SuperBuilder
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ResponseAudioDone extends BaseResponse {
    }

    @Getter
    @ToString(callSuper = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public abstract static class BaseResponseFunctionCallArguments extends BaseEvent {

        private String responseId;
        private String itemId;
        private Integer outputIndex;
        private String callId;

    }

    @Getter
    @ToString(callSuper = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ResponseFunctionCallArgumentsDelta extends BaseResponseFunctionCallArguments {

        private String delta;

    }

    @Getter
    @ToString(callSuper = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ResponseFunctionCallArgumentsDone extends BaseResponseFunctionCallArguments {

        private String arguments;

    }

    @Getter
    @ToString(callSuper = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class RateLimitsUpdated extends BaseEvent {

        private List<RateLimit> rateLimits;

    }

    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ErrorDetail {

        private String type;
        private String code;
        private String message;
        private String param;
        private String eventId;

    }

    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Conversation {

        private String id;
        private String object;

    }

    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Part {

        private String type;
        private String text;
        private String audio;
        private String transcript;

    }

    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class RateLimit {

        private String name;
        private Integer limit;
        private Integer remaining;
        private Double resetSeconds;

    }

}
