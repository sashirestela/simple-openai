package io.github.sashirestela.openai.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.github.sashirestela.openai.common.content.ImageDetail;
import io.github.sashirestela.slimvalidator.constraints.ObjectType;
import io.github.sashirestela.slimvalidator.constraints.Required;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

public abstract class Input {

    @NoArgsConstructor
    @Getter
    @ToString
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class InputMessage extends Input {

        @Required
        @ObjectType(baseClass = String.class)
        @ObjectType(baseClass = Content.class, firstGroup = true)
        private Object content;

        @Required
        private MessageRole role;

        private InputType type;

        private InputMessage(Object content, MessageRole role) {
            this.content = content;
            this.role = role;
            this.type = InputType.MESSAGE;
        }

        public static InputMessage of(Object content, MessageRole role) {
            return new InputMessage(content, role);
        }

    }

    @NoArgsConstructor
    @Getter
    @ToString
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ItemReference extends Input {

        @Required
        private String id;

        @Required
        private InputType type;

        private ItemReference(String id) {
            this.id = id;
            this.type = InputType.ITEM_REFERENCE;
        }

        public static ItemReference of(String id) {
            return new ItemReference(id);
        }

    }

    @Getter
    @Setter
    public abstract static class Content {

        protected ContentType type;

        public enum ContentType {

            @JsonProperty("input_text")
            INPUT_TEXT,

            @JsonProperty("input_image")
            INPUT_IMAGE,

            @JsonProperty("input_file")
            INPUT_FILE;

        }

        @NoArgsConstructor
        @Getter
        @ToString
        @JsonInclude(Include.NON_EMPTY)
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class TextInputContent extends Content {

            @Required
            private String text;

            private TextInputContent(String text) {
                this.text = text;
                this.type = ContentType.INPUT_TEXT;
            }

            public static TextInputContent of(String text) {
                return new TextInputContent(text);
            }

        }

        @NoArgsConstructor
        @Getter
        @ToString
        @JsonInclude(Include.NON_EMPTY)
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class ImageInputContent extends Content {

            @Required
            private ImageDetail detail;

            private String fileId;

            private String imageUrl;

            @Builder
            public ImageInputContent(ImageDetail detail, String fileId, String imageUrl) {
                this.type = ContentType.INPUT_IMAGE;
                this.detail = detail;
                this.fileId = fileId;
                this.imageUrl = imageUrl;
            }

            public static ImageInputContent of(ImageDetail detail) {
                return new ImageInputContent(detail, null, null);
            }

        }

        @NoArgsConstructor
        @Getter
        @ToString
        @JsonInclude(Include.NON_EMPTY)
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class FileInputContent extends Content {

            private String fileData;

            private String fileId;

            private String filename;

            @Builder
            public FileInputContent(String fileData, String fileId, String filename) {
                this.type = ContentType.INPUT_FILE;
                this.fileData = fileData;
                this.fileId = fileId;
                this.filename = filename;
            }

        }

    }

    @Getter
    @Setter
    public abstract static class Item extends Input {

        protected ItemType type;

        public enum ItemStatus {

            @JsonProperty("in_progress")
            IN_PROGRESS,

            @JsonProperty("completed")
            COMPLETED,

            @JsonProperty("incomplete")
            INCOMPLETE;

        }

        @NoArgsConstructor
        @Getter
        @ToString
        @JsonInclude(Include.NON_EMPTY)
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class InputMessageItem extends Item {

            @Required
            private List<Content> content;

            @Required
            private MessageRole role;

            private ItemStatus status;

            @Builder
            public InputMessageItem(List<Content> content, MessageRole role, ItemStatus status) {
                this.content = content;
                this.role = role;
                this.status = status;
                this.type = ItemType.MESSAGE;
            }

        }

        @NoArgsConstructor
        @Getter
        @ToString
        @JsonInclude(Include.NON_EMPTY)
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class OutputMessageItem extends Item {

            @Required
            private List<Object> content;

            @Required
            private String id;

            @Required
            private MessageRole role;

            @Required
            private ItemStatus status;

            @Builder
            public OutputMessageItem(List<Object> content, String id, ItemStatus status) {
                this.content = content;
                this.id = id;
                this.status = status;
                this.role = MessageRole.ASSISTANT;
                this.type = ItemType.MESSAGE;
            }

        }

    }

    public enum InputType {

        @JsonProperty("message")
        MESSAGE,

        @JsonProperty("item_reference")
        ITEM_REFERENCE;

    }

    public enum MessageRole {

        @JsonProperty("user")
        USER,

        @JsonProperty("assistant")
        ASSISTANT,

        @JsonProperty("system")
        SYSTEM,

        @JsonProperty("developer")
        DEVELOPER;

    }

    public enum ItemType {

        @JsonProperty("message")
        MESSAGE,

        @JsonProperty("file_search_call")
        FILE_SEARCH_CALL,

        @JsonProperty("computer_call")
        COMPUTER_CALL,

        @JsonProperty("computer_call_output")
        COMPUTER_CALL_OUTPUT,

        @JsonProperty("web_search_call")
        WEB_SEARCH_CALL,

        @JsonProperty("function_call")
        FUNCTION_CALL,

        @JsonProperty("function_call_output")
        FUNCTION_CALL_OUTPUT,

        @JsonProperty("reasoning")
        REASONING;

    }

}
