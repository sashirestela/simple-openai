package io.github.sashirestela.openai.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.github.sashirestela.openai.common.content.ImageDetail;
import io.github.sashirestela.slimvalidator.constraints.ObjectType;
import io.github.sashirestela.slimvalidator.constraints.Range;
import io.github.sashirestela.slimvalidator.constraints.Required;
import io.github.sashirestela.slimvalidator.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;
import lombok.ToString;

import java.util.List;
import java.util.Map;

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

            @Required
            private String fileId;

            @Required
            private String imageUrl;

            @Builder
            public ImageInputContent(ImageDetail detail, String fileId, String imageUrl) {
                this.detail = detail;
                this.fileId = fileId;
                this.imageUrl = imageUrl;
                this.type = ContentType.INPUT_IMAGE;
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
                this.fileData = fileData;
                this.fileId = fileId;
                this.filename = filename;
                this.type = ContentType.INPUT_FILE;
            }

        }

    }

    @Getter
    @Setter
    public abstract static class Item extends Input {

        protected ItemType type;

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
            private List<OutputContent> content;

            @Required
            private String id;

            @Required
            private MessageRole role;

            @Required
            private ItemStatus status;

            @Builder
            public OutputMessageItem(List<OutputContent> content, String id, ItemStatus status) {
                this.content = content;
                this.id = id;
                this.status = status;
                this.role = MessageRole.ASSISTANT;
                this.type = ItemType.MESSAGE;
            }

        }

        @NoArgsConstructor
        @Getter
        @ToString
        @JsonInclude(Include.NON_EMPTY)
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class FileSearchCallItem extends Item {

            @Required
            private String id;

            @Required
            @Singular
            private List<String> queries;

            @Required
            private SearchStatus status;

            @Singular
            private List<FileSearchResult> results;

            @Builder
            public FileSearchCallItem(String id, List<String> queries, SearchStatus status,
                    List<FileSearchResult> results) {
                this.id = id;
                this.queries = queries;
                this.status = status;
                this.results = results;
                this.type = ItemType.FILE_SEARCH_CALL;
            }

        }

        @NoArgsConstructor
        @Getter
        @ToString
        @JsonInclude(Include.NON_EMPTY)
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class ComputerCallItem extends Item {

            @Required
            private Action action;

            @Required
            private String callId;

            @Required
            private String id;

            @Required
            @Singular
            private List<SafetyCheck> pendingSafetyChecks;

            @Required
            private ItemStatus status;

            @Builder
            public ComputerCallItem(Action action, String callId, String id, List<SafetyCheck> pendingSafetyChecks,
                    ItemStatus status) {
                this.action = action;
                this.callId = callId;
                this.id = id;
                this.pendingSafetyChecks = pendingSafetyChecks;
                this.status = status;
                this.type = ItemType.COMPUTER_CALL;
            }

        }

        @NoArgsConstructor
        @Getter
        @ToString
        @JsonInclude(Include.NON_EMPTY)
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class ComputerCallOutputItem extends Item {

            @Required
            private String callId;

            @Required
            private ScreenshootImage output;

            @Singular
            private List<SafetyCheck> acknowledgedSafetyChecks;

            @Required
            private String id;

            @Required
            private ItemStatus status;

            @Builder
            public ComputerCallOutputItem(String callId, ScreenshootImage output,
                    List<SafetyCheck> acknowledgedSafetyChecks, String id, ItemStatus status) {
                this.callId = callId;
                this.output = output;
                this.acknowledgedSafetyChecks = acknowledgedSafetyChecks;
                this.id = id;
                this.status = status;
                this.type = ItemType.COMPUTER_CALL_OUTPUT;
            }

        }

        @NoArgsConstructor
        @Getter
        @ToString
        @JsonInclude(Include.NON_EMPTY)
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class WebSearchCallItem extends Item {

            @Required
            private String id;

            @Required
            private SearchStatus status;

            @Builder
            public WebSearchCallItem(String id, SearchStatus status) {
                this.id = id;
                this.status = status;
                this.type = ItemType.WEB_SEARCH_CALL;
            }

        }

        @NoArgsConstructor
        @Getter
        @ToString
        @JsonInclude(Include.NON_EMPTY)
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class FunctionCallItem extends Item {

            @Required
            private String arguments;

            @Required
            private String callId;

            @Required
            private String name;

            @Required
            private String id;

            @Required
            private ItemStatus status;

            @Builder
            public FunctionCallItem(String arguments, String callId, String name, String id, ItemStatus status) {
                this.arguments = arguments;
                this.callId = callId;
                this.name = name;
                this.id = id;
                this.status = status;
                this.type = ItemType.FUNCTION_CALL;
            }

        }

        @NoArgsConstructor
        @Getter
        @ToString
        @JsonInclude(Include.NON_EMPTY)
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class FunctionCallOutputItem extends Item {

            @Required
            private String callId;

            @Required
            private String output;

            private String id;

            private ItemStatus status;

            @Builder
            public FunctionCallOutputItem(String callId, String output, String id, ItemStatus status) {
                this.callId = callId;
                this.output = output;
                this.id = id;
                this.status = status;
                this.type = ItemType.FUNCTION_CALL_OUTPUT;
            }

        }

        @NoArgsConstructor
        @Getter
        @ToString
        @JsonInclude(Include.NON_EMPTY)
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class ReasoningItem extends Item {

            @Required
            private String id;

            @Required
            private List<ReasoningContent> summary;

            private String encryptedContent;

            private ItemStatus status;

            @Builder
            public ReasoningItem(String id, List<ReasoningContent> summary, String encryptedContent, ItemStatus status) {
                this.id = id;
                this.summary = summary;
                this.encryptedContent = encryptedContent;
                this.status = status;
                this.type = ItemType.REASONING;
            }

        }

    }

    @Getter
    @Setter
    public abstract static class OutputContent {

        protected OutputContentType type;

        @NoArgsConstructor
        @Getter
        @ToString
        @JsonInclude(Include.NON_EMPTY)
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class TextOutputContent extends OutputContent {

            @Required
            @Singular
            private List<Citation> annotations;

            @Required
            private String text;

            private TextOutputContent(List<Citation> annotations, String text) {
                this.annotations = annotations;
                this.text = text;
                this.type = OutputContentType.OUTPUT_TEXT;
            }

            public static TextOutputContent of(List<Citation> annotations, String text) {
                return new TextOutputContent(annotations, text);
            }

        }

        @NoArgsConstructor
        @Getter
        @ToString
        @JsonInclude(Include.NON_EMPTY)
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class RefusalOutputContent extends OutputContent {

            private String refusal;

            private RefusalOutputContent(String refusal) {
                this.refusal = refusal;
                this.type = OutputContentType.REFUSAL;
            }

            public static RefusalOutputContent of(String refusal) {
                return new RefusalOutputContent(refusal);
            }

        }

    }

    @Getter
    @Setter
    public abstract static class Citation {

        protected CitationType type;

        @NoArgsConstructor
        @Getter
        @ToString
        @JsonInclude(Include.NON_EMPTY)
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class FileCitation extends Citation {

            @Required
            private String fileId;

            @Required
            private Integer index;

            private FileCitation(String fileId, Integer index) {
                this.fileId = fileId;
                this.index = index;
                this.type = CitationType.FILE_CITATION;
            }

            public static FileCitation of(String fileId, Integer index) {
                return new FileCitation(fileId, index);
            }

        }

        @NoArgsConstructor
        @Getter
        @ToString
        @JsonInclude(Include.NON_EMPTY)
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class UrlCitation extends Citation {

            @Required
            private Integer endIndex;

            @Required
            private Integer startIndex;

            @Required
            private String title;

            @Required
            private String url;

            @Builder
            public UrlCitation(Integer endIndex, Integer startIndex, String title, String url) {
                this.endIndex = endIndex;
                this.startIndex = startIndex;
                this.title = title;
                this.url = url;
                this.type = CitationType.URL_CITATION;
            }

        }

        @NoArgsConstructor
        @Getter
        @ToString
        @JsonInclude(Include.NON_EMPTY)
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class FilePath extends Citation {

            @Required
            private String fileId;

            @Required
            private Integer index;

            private FilePath(String fileId, Integer index) {
                this.fileId = fileId;
                this.index = index;
                this.type = CitationType.FILE_PATH;
            }

            public static FilePath of(String fileId, Integer index) {
                return new FilePath(fileId, index);
            }

        }

    }

    @NoArgsConstructor
    @Getter
    @ToString
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class FileSearchResult {

        @Size(max = 16)
        private Map<String, Object> attributes;

        private String fileId;

        private String filename;

        @Range(min = 0.0, max = 1.0)
        private Double score;

        private String text;

    }

    @NoArgsConstructor
    @Getter
    @ToString
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class SafetyCheck {

        @Required
        private String id;

        private String code;

        private String message;

        @Builder
        public SafetyCheck(String id, String code, String message) {
            this.id = id;
            this.code = code;
            this.message = message;
        }

    }

    @NoArgsConstructor
    @Getter
    @ToString
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ScreenshootImage {

        private String fileId;
        private String imageUrl;
        private String type;

        @Builder
        public ScreenshootImage(String fileId, String imageUrl) {
            this.fileId = fileId;
            this.imageUrl = imageUrl;
            this.type = "computer_screenshot";
        }

    }

    @NoArgsConstructor
    @Getter
    @ToString
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ReasoningContent {

        @Required
        private String text;

        private String type;

        private ReasoningContent(String text) {
            this.text = text;
            this.type = "summary_text";
        }

        public static ReasoningContent of(String text) {
            return new ReasoningContent(text);
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

    public enum ContentType {

        @JsonProperty("input_text")
        INPUT_TEXT,

        @JsonProperty("input_image")
        INPUT_IMAGE,

        @JsonProperty("input_file")
        INPUT_FILE;

    }

    public enum OutputContentType {

        @JsonProperty("output_text")
        OUTPUT_TEXT,

        @JsonProperty("refusal")
        REFUSAL;

    }

    public enum ItemStatus {

        @JsonProperty("in_progress")
        IN_PROGRESS,

        @JsonProperty("completed")
        COMPLETED,

        @JsonProperty("incomplete")
        INCOMPLETE;

    }

    public enum SearchStatus {

        @JsonProperty("in_progress")
        IN_PROGRESS,

        @JsonProperty("searching")
        SEARCHING,

        @JsonProperty("completed")
        COMPLETED,

        @JsonProperty("incomplete")
        INCOMPLETE,

        @JsonProperty("failed")
        FAILED;

    }

    public enum CitationType {

        @JsonProperty("file_citation")
        FILE_CITATION,

        @JsonProperty("url_citation")
        URL_CITATION,

        @JsonProperty("file_path")
        FILE_PATH;

    }

}
