package io.github.sashirestela.openai.domain.chat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.github.sashirestela.cleverclient.util.CommonUtil;
import io.github.sashirestela.openai.common.content.ContentPart.ChatContentPart;
import io.github.sashirestela.openai.common.tool.ToolCall;
import io.github.sashirestela.slimvalidator.constraints.ObjectType;
import io.github.sashirestela.slimvalidator.constraints.Required;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
public abstract class ChatMessage {

    protected ChatRole role;

    public enum ChatRole {

        @JsonProperty("system")
        SYSTEM,

        @JsonProperty("user")
        USER,

        @JsonProperty("assistant")
        ASSISTANT,

        @JsonProperty("tool")
        TOOL;

    }

    @Getter
    @ToString
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class SystemMessage extends ChatMessage {

        @Required
        private String content;

        private String name;

        private SystemMessage(String content, String name) {
            this.role = ChatRole.SYSTEM;
            this.content = content;
            this.name = name;
        }

        public static SystemMessage of(String content, String name) {
            return new SystemMessage(content, name);
        }

        public static SystemMessage of(String content) {
            return new SystemMessage(content, null);
        }

    }

    @Getter
    @ToString
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class UserMessage extends ChatMessage {

        @Required
        @ObjectType(baseClass = String.class)
        @ObjectType(baseClass = ChatContentPart.class, firstGroup = true)
        private Object content;

        private String name;

        private UserMessage(Object content, String name) {
            this.role = ChatRole.USER;
            this.content = content;
            this.name = name;
        }

        public static UserMessage of(Object content, String name) {
            return new UserMessage(content, name);
        }

        public static UserMessage of(Object content) {
            return new UserMessage(content, null);
        }

    }

    @Getter
    @ToString
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class AssistantMessage extends ChatMessage {

        @JsonInclude
        @ObjectType(baseClass = String.class)
        @ObjectType(baseClass = ChatContentPart.class, firstGroup = true)
        private Object content;

        private String refusal;

        private String name;

        private Audio audio;

        private List<ToolCall> toolCalls;

        @Builder
        public AssistantMessage(String content, String refusal, String name, String audioId, List<ToolCall> toolCalls) {
            this.role = ChatRole.ASSISTANT;
            this.content = content;
            this.refusal = refusal;
            this.name = name;
            this.audio = CommonUtil.isNullOrEmpty(audioId) ? null : new Audio(audioId);
            this.toolCalls = toolCalls;
        }

        @Getter
        @ToString
        static class Audio {

            @Required
            private String id;

            public Audio(String id) {
                this.id = id;
            }

        }

        public static AssistantMessage of(String content) {
            return AssistantMessage.builder().content(content).build();
        }

        public static AssistantMessage of(List<ToolCall> toolCalls) {
            return AssistantMessage.builder().toolCalls(toolCalls).build();
        }

    }

    @Getter
    @ToString
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ToolMessage extends ChatMessage {

        @Required
        private String content;

        @Required
        private String toolCallId;

        private ToolMessage(String content, String toolCallId) {
            this.role = ChatRole.TOOL;
            this.content = content;
            this.toolCallId = toolCallId;
        }

        public static ToolMessage of(String content, String toolCallId) {
            return new ToolMessage(content, toolCallId);
        }

    }

    @NoArgsConstructor
    @Getter
    @Setter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ResponseMessage extends ChatMessage {

        private String content;
        private List<ToolCall> toolCalls;
        private String refusal;
        private AudioResponse audio;

        @NoArgsConstructor
        @Getter
        @Setter
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class AudioResponse {

            private String id;
            private Integer expiresAt;
            private String data;
            private String transcript;

        }

    }

}
