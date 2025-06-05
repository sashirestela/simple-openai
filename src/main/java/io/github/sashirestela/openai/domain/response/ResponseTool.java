package io.github.sashirestela.openai.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.github.sashirestela.openai.common.function.FunctionDef;
import io.github.sashirestela.openai.domain.assistant.RankingOption;
import io.github.sashirestela.slimvalidator.constraints.ObjectType;
import io.github.sashirestela.slimvalidator.constraints.Range;
import io.github.sashirestela.slimvalidator.constraints.Required;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;
import lombok.ToString;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ResponseTool.FileSearchResponseTool.class, name = "file_search"),
        @JsonSubTypes.Type(value = ResponseTool.FunctionResponseTool.class, name = "function"),
        @JsonSubTypes.Type(value = ResponseTool.ComputerResponseTool.class, name = "computer_use_preview"),
        @JsonSubTypes.Type(value = ResponseTool.WebSearchResponseTool.class, name = "web_search_preview"),
        @JsonSubTypes.Type(value = ResponseTool.McpResponseTool.class, name = "mcp"),
        @JsonSubTypes.Type(value = ResponseTool.CodeInterpreterResponseTool.class, name = "code_interpreter"),
        @JsonSubTypes.Type(value = ResponseTool.ImageGenerationResponseTool.class, name = "image_generation"),
        @JsonSubTypes.Type(value = ResponseTool.LocalShellResponseTool.class, name = "local_shell"),
})
@Getter
@Setter
public abstract class ResponseTool {

    protected ResponseToolType type;

    @NoArgsConstructor
    @Getter
    @ToString
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class FileSearchResponseTool extends ResponseTool {

        @Required
        private List<String> vectorStoreIds;

        @JsonDeserialize(using = FilterDeserializer.class)
        private Filter filters;

        @Range(min = 1, max = 50)
        private Integer maxNumResults;

        private RankingOption rankingOptions;

        @Builder
        public FileSearchResponseTool(List<String> vectorStoreIds, Filter filters, Integer maxNumResults,
                RankingOption rankingOptions) {
            this.vectorStoreIds = vectorStoreIds;
            this.filters = filters;
            this.maxNumResults = maxNumResults;
            this.rankingOptions = rankingOptions;
            this.type = ResponseToolType.FILE_SEARCH;
        }

        public static FileSearchResponseTool of(List<String> vectorStoreIds) {
            return FileSearchResponseTool.builder().vectorStoreIds(vectorStoreIds).build();
        }

    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @ToString
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class FunctionResponseTool extends ResponseTool {

        @Required
        private String name;

        @Required
        private JsonNode parameters;

        @Required
        private Boolean strict;

        private String description;

        public static FunctionResponseTool function(FunctionDef funcDef) {
            var funcTool = new FunctionResponseTool(
                    funcDef.getName(),
                    funcDef.getSchemaConverter().convert(funcDef.getFunctionalClass()),
                    funcDef.getStrict(),
                    funcDef.getDescription());
            funcTool.setType(ResponseToolType.FUNCTION);
            return funcTool;
        }

        public static List<FunctionResponseTool> functions(List<FunctionDef> funcDefList) {
            return funcDefList.stream()
                    .map(FunctionResponseTool::function)
                    .collect(Collectors.toList());
        }

    }

    @NoArgsConstructor
    @Getter
    @ToString
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ComputerResponseTool extends ResponseTool {

        @Required
        private Integer displayHeight;

        @Required
        private Integer displayWidth;

        @Required
        private Environment environment;

        @Builder
        public ComputerResponseTool(Integer displayHeight, Integer displayWidth, Environment environment) {
            this.displayHeight = displayHeight;
            this.displayWidth = displayWidth;
            this.environment = environment;
            this.type = ResponseToolType.COMPUTER_USE_PREVIEW;
        }

    }

    @NoArgsConstructor
    @Getter
    @ToString
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class WebSearchResponseTool extends ResponseTool {

        private ContextSize searchContextSize;

        private Location userLocation;

        @Builder
        public WebSearchResponseTool(ContextSize searchContextSize, Location userLocation) {
            this.searchContextSize = searchContextSize;
            this.userLocation = userLocation;
            this.type = ResponseToolType.WEB_SEARCH_PREVIEW;
        }

        public static WebSearchResponseTool of() {
            return WebSearchResponseTool.builder().build();
        }

    }

    @NoArgsConstructor
    @Getter
    @ToString
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class McpResponseTool extends ResponseTool {

        @Required
        private String serverLabel;

        @Required
        private String serverUrl;

        @ObjectType(baseClass = String.class, firstGroup = true)
        @ObjectType(baseClass = McpListTools.class)
        @JsonDeserialize(using = AllowedToolsDeserializer.class)
        private Object allowedTools;

        private Map<String, String> headers;

        @ObjectType(baseClass = McpToolApprovalSetting.class)
        @ObjectType(baseClass = McpToolApprovalFilter.class)
        @JsonDeserialize(using = RequireApprovalDeserializer.class)
        private Object requireApproval;

        @Builder
        public McpResponseTool(String serverLabel, String serverUrl, Object allowedTools, Map<String, String> headers,
                Object requireApproval) {
            this.serverLabel = serverLabel;
            this.serverUrl = serverUrl;
            this.allowedTools = allowedTools;
            this.headers = headers;
            this.requireApproval = requireApproval;
            this.type = ResponseToolType.MCP;
        }

    }

    @NoArgsConstructor
    @Getter
    @ToString
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class CodeInterpreterResponseTool extends ResponseTool {

        @Required
        @ObjectType(baseClass = String.class)
        @ObjectType(baseClass = ContainerAuto.class)
        @JsonDeserialize(using = ContainerDeserializer.class)
        private Object container;

        private CodeInterpreterResponseTool(Object container) {
            this.container = container;
            this.type = ResponseToolType.CODE_INTERPRETER;
        }

        public static CodeInterpreterResponseTool of(Object container) {
            return new CodeInterpreterResponseTool(container);
        }

    }

    @NoArgsConstructor
    @Getter
    @ToString
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ImageGenerationResponseTool extends ResponseTool {

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
        public ImageGenerationResponseTool(ImageBackground background, ImageMask inputImageMask, String model,
                String moderation, Integer outputCompression, ImageFormat outputFormat, Integer partialImages,
                ImageQuality quality, String size) {
            this.background = background;
            this.inputImageMask = inputImageMask;
            this.model = model;
            this.moderation = moderation;
            this.outputCompression = outputCompression;
            this.outputFormat = outputFormat;
            this.partialImages = partialImages;
            this.quality = quality;
            this.size = size;
            this.type = ResponseToolType.IMAGE_GENERATION;
        }

    }

    @NoArgsConstructor
    @Getter
    @ToString
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class LocalShellResponseTool extends ResponseTool {

        public static LocalShellResponseTool of() {
            var localShellTool = new LocalShellResponseTool();
            localShellTool.setType(ResponseToolType.LOCAL_SHELL);
            return localShellTool;
        }

    }

    public static interface Filter {

        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        @Getter
        @ToString
        @JsonInclude(Include.NON_EMPTY)
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        class ComparisonFilter implements Filter {

            @Required
            private String key;

            @Required
            private ComparisonOperator type;

            @Required
            @ObjectType(baseClass = String.class)
            @ObjectType(baseClass = Double.class)
            @ObjectType(baseClass = Boolean.class)
            private Object value;

        }

        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        @Getter
        @ToString
        @JsonInclude(Include.NON_EMPTY)
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        class CompoundFilter implements Filter {

            @Required
            @Singular
            @JsonDeserialize(contentUsing = FilterDeserializer.class)
            private List<Filter> filters;

            @Required
            private LogicalOperator type;

        }

    }

    @NoArgsConstructor
    @Getter
    @ToString
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Location {

        private String type;
        private String city;
        private String country;
        private String region;
        private String timezone;

        @Builder
        public Location(String type, String city, String country, String region, String timezone) {
            this.type = "approximate";
            this.city = city;
            this.country = country;
            this.region = region;
            this.timezone = timezone;
        }

    }

    @NoArgsConstructor
    @Getter
    @ToString
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class McpListTools {

        private List<String> toolNames;

        private McpListTools(List<String> toolNames) {
            this.toolNames = toolNames;
        }

        public static McpListTools of(List<String> toolNames) {
            return new McpListTools(toolNames);
        }

    }

    @NoArgsConstructor
    @Getter
    @ToString
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class McpToolApprovalFilter {

        private McpListTools always;
        private McpListTools never;

        @Builder
        public McpToolApprovalFilter(McpListTools always, McpListTools never) {
            this.always = always;
            this.never = never;
        }

    }

    @NoArgsConstructor
    @Getter
    @ToString
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ContainerAuto {

        private String type;
        private List<String> fileIds;

        private ContainerAuto(List<String> fileIds) {
            this.type = "auto";
            this.fileIds = fileIds;
        }

        public static ContainerAuto of(List<String> fileIds) {
            return new ContainerAuto(fileIds);
        }

        public static ContainerAuto of() {
            return new ContainerAuto(null);
        }

    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    @ToString
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ImageMask {

        private String fileId;
        private String imageUrl;

    }

    public enum ResponseToolType {

        @JsonProperty("file_search")
        FILE_SEARCH,

        @JsonProperty("function")
        FUNCTION,

        @JsonProperty("computer_use_preview")
        COMPUTER_USE_PREVIEW,

        @JsonProperty("web_search_preview")
        WEB_SEARCH_PREVIEW,

        @JsonProperty("mcp")
        MCP,

        @JsonProperty("code_interpreter")
        CODE_INTERPRETER,

        @JsonProperty("image_generation")
        IMAGE_GENERATION,

        @JsonProperty("local_shell")
        LOCAL_SHELL;

    }

    public enum ComparisonOperator {

        @JsonProperty("eq")
        EQ,

        @JsonProperty("ne")
        NE,

        @JsonProperty("gt")
        GT,

        @JsonProperty("gte")
        GTE,

        @JsonProperty("lt")
        LT,

        @JsonProperty("lte")
        LTE;

    }

    public enum LogicalOperator {

        @JsonProperty("and")
        AND,

        @JsonProperty("or")
        OR;

    }

    public enum Environment {

        @JsonProperty("mac")
        MAC,

        @JsonProperty("windows")
        WINDOWS,

        @JsonProperty("ubuntu")
        UBUNTU,

        @JsonProperty("browser")
        BROWSER;

    }

    public enum ContextSize {

        @JsonProperty("low")
        LOW,

        @JsonProperty("medium")
        MEDIUM,

        @JsonProperty("high")
        HIGH;

    }

    public enum McpToolApprovalSetting {

        @JsonProperty("always")
        ALWAYS,

        @JsonProperty("never")
        NEVER;

    }

    public enum ImageBackground {

        @JsonProperty("transparent")
        TRANSPARENT,

        @JsonProperty("opaque")
        OPAQUE,

        @JsonProperty("auto")
        AUTO;

    }

    public enum ImageFormat {

        @JsonProperty("png")
        PNG,

        @JsonProperty("webp")
        WEBP,

        @JsonProperty("jpeg")
        JPEG;

    }

    public enum ImageQuality {

        @JsonProperty("low")
        LOW,

        @JsonProperty("medium")
        MEDIUM,

        @JsonProperty("high")
        HIGH,

        @JsonProperty("auto")
        AUTO;

    }

    public static class AllowedToolsDeserializer extends JsonDeserializer<Object> {

        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            JsonNode node = p.getCodec().readTree(p);
            ObjectMapper mapper = (ObjectMapper) p.getCodec();

            if (node.isArray()) {
                List<String> toolNames = new ArrayList<>();
                for (JsonNode element : node) {
                    if (element.isTextual()) {
                        toolNames.add(element.asText());
                    }
                }
                return toolNames;
            } else if (node.isObject() && node.has("tool_names")) {
                return mapper.treeToValue(node, ResponseTool.McpListTools.class);
            } else {
                throw new IOException(
                        "Unable to deserialize allowedTools: expected array of strings or McpListTools object");
            }
        }

    }

    public static class RequireApprovalDeserializer extends JsonDeserializer<Object> {

        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            JsonNode node = p.getCodec().readTree(p);
            ObjectMapper mapper = (ObjectMapper) p.getCodec();

            if (node.isTextual()) {
                String value = node.asText();
                try {
                    return ResponseTool.McpToolApprovalSetting.valueOf(value.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new IOException("Invalid McpToolApprovalSetting value: " + value, e);
                }
            } else if (node.isObject() && (node.has("always") || node.has("never"))) {
                return mapper.treeToValue(node, ResponseTool.McpToolApprovalFilter.class);
            } else {
                throw new IOException(
                        "Unable to deserialize requireApproval: expected string (enum) or McpToolApprovalFilter object");
            }
        }

    }

    public static class ContainerDeserializer extends JsonDeserializer<Object> {

        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            JsonToken token = p.getCurrentToken();

            if (token == JsonToken.VALUE_STRING) {
                return p.getValueAsString();
            } else if (token == JsonToken.START_OBJECT) {
                JsonNode node = p.getCodec().readTree(p);
                ObjectMapper mapper = (ObjectMapper) p.getCodec();

                if (node.has("type") && "auto".equals(node.get("type").asText())) {
                    return mapper.treeToValue(node, ResponseTool.ContainerAuto.class);
                } else {
                    throw new IOException(
                            "Invalid object for container field: expected ContainerAuto with type 'auto'");
                }
            } else {
                throw new IOException("Unable to deserialize container: expected string or ContainerAuto object");
            }
        }

    }

}
