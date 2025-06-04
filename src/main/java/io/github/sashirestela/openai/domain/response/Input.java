package io.github.sashirestela.openai.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.github.sashirestela.openai.common.content.ImageDetail;
import io.github.sashirestela.openai.domain.response.ResponseTool.ImageBackground;
import io.github.sashirestela.openai.domain.response.ResponseTool.ImageFormat;
import io.github.sashirestela.openai.domain.response.ResponseTool.ImageMask;
import io.github.sashirestela.openai.domain.response.ResponseTool.ImageQuality;
import io.github.sashirestela.slimvalidator.constraints.ObjectType;
import io.github.sashirestela.slimvalidator.constraints.Range;
import io.github.sashirestela.slimvalidator.constraints.Required;
import io.github.sashirestela.slimvalidator.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

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

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
    @JsonSubTypes({
            @JsonSubTypes.Type(value = Content.TextInputContent.class, name = "input_text"),
            @JsonSubTypes.Type(value = Content.ImageInputContent.class, name = "input_image"),
            @JsonSubTypes.Type(value = Content.FileInputContent.class, name = "input_file"),
    })
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

            public static ImageInputContent of(String imageUrl) {
                return ImageInputContent.builder().imageUrl(imageUrl).detail(ImageDetail.AUTO).build();
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
        protected String id;

        @NoArgsConstructor
        @Getter
        @ToString
        @JsonInclude(Include.NON_EMPTY)
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class InputMessageItem extends Item {

            @Required
            protected MessageRole role;

            protected ItemStatus status;

            @Required
            private List<Content> content;

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
            protected MessageRole role;

            protected ItemStatus status;

            @Required
            private List<OutputContent> content;

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
            private ScreenshotImage output;

            @Singular
            private List<SafetyCheck> acknowledgedSafetyChecks;

            @Required
            private ItemStatus status;

            @Builder
            public ComputerCallOutputItem(String callId, ScreenshotImage output,
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
            private List<ReasoningContent> summary;

            private String encryptedContent;

            private ItemStatus status;

            @Builder
            public ReasoningItem(String id, List<ReasoningContent> summary, String encryptedContent,
                    ItemStatus status) {
                this.id = id;
                this.summary = summary;
                this.encryptedContent = encryptedContent;
                this.status = status;
                this.type = ItemType.REASONING;
            }

        }

        @NoArgsConstructor
        @Getter
        @ToString
        @JsonInclude(Include.NON_EMPTY)
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class ImageGenerationCallItem extends Item {

            @Required
            private String result;

            @Required
            private ItemStatus status;
            
            private ImageBackground background;
            private ImageMask inputImageMask;
            private String model;
            private String moderation;
            private Integer outputCompression;
            private ImageFormat outputFormat;
            private Integer partialImages;
            private ImageQuality quality;
            private String size;

            @Builder
            public ImageGenerationCallItem(String id, String result, ItemStatus status) {
                this.id = id;
                this.result = result;
                this.status = status;
                this.type = ItemType.IMAGE_GENERATION_CALL;
            }

        }

        @NoArgsConstructor
        @Getter
        @ToString
        @JsonInclude(Include.NON_EMPTY)
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class CodeInterpreterCallItem extends Item {

            @Required
            private String code;

            @Required
            private List<CodeInterpreterOutput> results;

            @Required
            private ItemStatus status;

            private String containerId;

            @Builder
            public CodeInterpreterCallItem(String id, String code, List<CodeInterpreterOutput> results,
                    ItemStatus status, String containerId) {
                this.id = id;
                this.code = code;
                this.results = results;
                this.status = status;
                this.containerId = containerId;
                this.type = ItemType.CODE_INTERPRETER_CALL;
            }

        }

        @NoArgsConstructor
        @Getter
        @ToString
        @JsonInclude(Include.NON_EMPTY)
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class LocalShellCallItem extends Item {

            @Required
            private ShellAction action;

            @Required
            private String callId;

            @Required
            private ItemStatus status;

            @Builder
            public LocalShellCallItem(String id, ShellAction action, String callId, ItemStatus status) {
                this.id = id;
                this.action = action;
                this.callId = callId;
                this.status = status;
                this.type = ItemType.LOCAL_SHELL_CALL;
            }

        }

        @NoArgsConstructor
        @Getter
        @ToString
        @JsonInclude(Include.NON_EMPTY)
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class LocalShellCallOutputItem extends Item {

            @Required
            private String output;

            private ItemStatus status;

            @Builder
            public LocalShellCallOutputItem(String id, String output, ItemStatus status) {
                this.id = id;
                this.output = output;
                this.status = status;
                this.type = ItemType.LOCAL_SHELL_CALL_OUTPUT;
            }

        }

        @NoArgsConstructor
        @Getter
        @ToString
        @JsonInclude(Include.NON_EMPTY)
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class McpListToolsItem extends Item {

            @Required
            private String serverLabel;

            @Required
            private List<McpTool> tools;

            private String error;

            @Builder
            public McpListToolsItem(String id, String serverLabel, List<McpTool> tools, String error) {
                this.id = id;
                this.serverLabel = serverLabel;
                this.tools = tools;
                this.error = error;
                this.type = ItemType.MCP_LIST_TOOLS;
            }

        }

        @NoArgsConstructor
        @Getter
        @ToString
        @JsonInclude(Include.NON_EMPTY)
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class McpApprovalRequestItem extends Item {

            @Required
            private String arguments;

            @Required
            private String name;

            @Required
            private String serverLabel;

            @Builder
            public McpApprovalRequestItem(String id, String arguments, String name, String serverLabel) {
                this.id = id;
                this.arguments = arguments;
                this.name = name;
                this.serverLabel = serverLabel;
                this.type = ItemType.MCP_APPROVAL_REQUEST;
            }

        }

        @NoArgsConstructor
        @Getter
        @ToString
        @JsonInclude(Include.NON_EMPTY)
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class McpApprovalResponseItem extends Item {

            @Required
            private String approvalRequestId;

            @Required
            private Boolean approve;

            private String reason;

            @Builder
            public McpApprovalResponseItem(String id, String approvalRequestId, Boolean approve, String reason) {
                this.id = id;
                this.approvalRequestId = approvalRequestId;
                this.approve = approve;
                this.reason = reason;
                this.type = ItemType.MCP_APPROVAL_RESPONSE;
            }

        }

        @NoArgsConstructor
        @Getter
        @ToString
        @JsonInclude(Include.NON_EMPTY)
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class McpCallItem extends Item {

            @Required
            private String arguments;

            @Required
            private String name;

            @Required
            private String serverLabel;

            private String error;
            private String output;
            private String approvalRequestId;

            @Builder
            public McpCallItem(String id, String arguments, String name, String serverLabel, String error,
                    String output, String approvalRequestId) {
                this.id = id;
                this.arguments = arguments;
                this.name = name;
                this.serverLabel = serverLabel;
                this.error = error;
                this.output = output;
                this.approvalRequestId = approvalRequestId;
                this.type = ItemType.MCP_CALL;
            }

        }

    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
    @JsonSubTypes({
            @JsonSubTypes.Type(value = OutputContent.TextOutputContent.class, name = "output_text"),
            @JsonSubTypes.Type(value = OutputContent.RefusalOutputContent.class, name = "refusal"),
    })
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

            private List<LogProb> logprobs;

            @Builder
            public TextOutputContent(List<Citation> annotations, String text, List<LogProb> logprobs) {
                this.annotations = annotations;
                this.text = text;
                this.logprobs = logprobs;
                this.type = OutputContentType.OUTPUT_TEXT;
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

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
    @JsonSubTypes({
            @JsonSubTypes.Type(value = Citation.FileCitation.class, name = "file_citation"),
            @JsonSubTypes.Type(value = Citation.UrlCitation.class, name = "url_citation"),
            @JsonSubTypes.Type(value = Citation.ContainerFileCitation.class, name = "container_file_citation"),
            @JsonSubTypes.Type(value = Citation.FilePath.class, name = "file_path"),
    })
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

            @Required
            private String filename;

            @Builder
            public FileCitation(String fileId, Integer index, String filename) {
                this.fileId = fileId;
                this.index = index;
                this.filename = filename;
                this.type = CitationType.FILE_CITATION;
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
        public static class ContainerFileCitation extends Citation {

            @Required
            private String containerId;

            @Required
            private Integer endIndex;

            @Required
            private Integer startIndex;

            @Required
            private String fileId;

            @Builder
            public ContainerFileCitation(String containerId, Integer endIndex, Integer startIndex, String fileId) {
                this.containerId = containerId;
                this.endIndex = endIndex;
                this.startIndex = startIndex;
                this.fileId = fileId;
                this.type = CitationType.CONTAINER_FILE_CITATION;
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

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
    @JsonSubTypes({
            @JsonSubTypes.Type(value = CodeInterpreterOutput.TextOutput.class, name = "logs"),
            @JsonSubTypes.Type(value = CodeInterpreterOutput.FileOutput.class, name = "files"),
    })
    @Getter
    @Setter
    public abstract static class CodeInterpreterOutput {

        protected CodeInterpreterOutputType type;

        @NoArgsConstructor
        @Getter
        @ToString
        @JsonInclude(Include.NON_EMPTY)
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class TextOutput extends CodeInterpreterOutput {

            @Required
            private String logs;

            private TextOutput(String logs) {
                this.logs = logs;
                this.type = CodeInterpreterOutputType.LOGS;
            }

            public static TextOutput of(String logs) {
                return new TextOutput(logs);
            }

        }

        @NoArgsConstructor
        @Getter
        @ToString
        @JsonInclude(Include.NON_EMPTY)
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class FileOutput extends CodeInterpreterOutput {

            @Required
            private List<CodeInterpreterFile> files;

            private FileOutput(List<CodeInterpreterFile> files) {
                this.files = files;
                this.type = CodeInterpreterOutputType.FILES;
            }

            public static FileOutput of(List<CodeInterpreterFile> files) {
                return new FileOutput(files);
            }

        }

    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
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
    @AllArgsConstructor
    @Builder
    @Getter
    @ToString
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class SafetyCheck {

        @Required
        private String id;

        private String code;

        private String message;

    }

    @NoArgsConstructor
    @Getter
    @ToString
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ScreenshotImage {

        private String fileId;
        private String imageUrl;
        private String type;

        @Builder
        public ScreenshotImage(String fileId, String imageUrl) {
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

    @NoArgsConstructor
    @Getter
    @ToString
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class CodeInterpreterFile {

        @Required
        private String fileId;

        @Required
        private String mimeType;

        private CodeInterpreterFile(String fileId, String mimeType) {
            this.fileId = fileId;
            this.mimeType = mimeType;
        }

        public static CodeInterpreterFile of(String fileId, String mimeType) {
            return new CodeInterpreterFile(fileId, mimeType);
        }

    }

    @NoArgsConstructor
    @Getter
    @ToString
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ShellAction {

        @Required
        private List<String> command;

        @Required
        private Object env;

        @Required
        private String type;

        private Integer timeoutMs;
        private String user;
        private String workingDirectory;

        @Builder
        public ShellAction(List<String> command, Object env, Integer timeoutMs, String user, String workingDirectory) {
            this.command = command;
            this.env = env;
            this.timeoutMs = timeoutMs;
            this.user = user;
            this.workingDirectory = workingDirectory;
            this.type = "exec";
        }

    }

    @NoArgsConstructor
    @Getter
    @ToString
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class McpTool {

        @Required
        private JsonNode inputSchema;

        @Required
        private String name;

        private Object annotations;

        private String description;

        @Builder
        public McpTool(JsonNode inputSchema, String name, Object annotations, String description) {
            this.inputSchema = inputSchema;
            this.name = name;
            this.annotations = annotations;
            this.description = description;
        }

    }

    @NoArgsConstructor
    @SuperBuilder
    @Getter
    @ToString
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class BasicLogProb {

        @Required
        protected List<Integer> bytes;

        @Required
        protected Double logprob;

        @Required
        protected String token;

    }

    @NoArgsConstructor
    @SuperBuilder
    @Getter
    @ToString
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class LogProb extends BasicLogProb {

        @Required
        private List<BasicLogProb> topLogprobs;

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
        REASONING,

        @JsonProperty("image_generation_call")
        IMAGE_GENERATION_CALL,

        @JsonProperty("code_interpreter_call")
        CODE_INTERPRETER_CALL,

        @JsonProperty("local_shell_call")
        LOCAL_SHELL_CALL,

        @JsonProperty("local_shell_call_output")
        LOCAL_SHELL_CALL_OUTPUT,

        @JsonProperty("mcp_list_tools")
        MCP_LIST_TOOLS,

        @JsonProperty("mcp_approval_request")
        MCP_APPROVAL_REQUEST,

        @JsonProperty("mcp_approval_response")
        MCP_APPROVAL_RESPONSE,

        @JsonProperty("mcp_call")
        MCP_CALL;

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

        @JsonProperty("container_file_citation")
        CONTAINER_FILE_CITATION,

        @JsonProperty("file_path")
        FILE_PATH;

    }

    public enum CodeInterpreterOutputType {

        @JsonProperty("logs")
        LOGS,

        @JsonProperty("files")
        FILES;

    }

}
