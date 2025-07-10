package io.github.sashirestela.openai.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.github.sashirestela.slimvalidator.constraints.Required;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

public interface ResponseToolChoice {

    enum HostedToolType {

        @JsonProperty("file_search")
        FILE_SEARCH,

        @JsonProperty("web_search_preview")
        WEB_SEARCH_PREVIEW,

        @JsonProperty("computer_use_preview")
        COMPUTER_USE_PREVIEW,

        @JsonProperty("code_interpreter")
        CODE_INTERPRETER,

        @JsonProperty("image_generation")
        IMAGE_GENERATION,

        @JsonProperty("mcp")
        MCP;

    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor
    @Getter
    @ToString
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    class HostedTool {

        public static final HostedTool FILE_SEARCH = new HostedTool(HostedToolType.FILE_SEARCH);
        public static final HostedTool WEB_SEARCH_PREVIEW = new HostedTool(HostedToolType.WEB_SEARCH_PREVIEW);
        public static final HostedTool COMPUTER_USE_PREVIEW = new HostedTool(HostedToolType.COMPUTER_USE_PREVIEW);
        public static final HostedTool CODE_INTERPRETER = new HostedTool(HostedToolType.CODE_INTERPRETER);
        public static final HostedTool IMAGE_GENERATION = new HostedTool(HostedToolType.IMAGE_GENERATION);
        public static final HostedTool MCP = new HostedTool(HostedToolType.MCP);

        private HostedToolType type;

    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor
    @Getter
    @ToString
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    class FunctionTool {

        @Required
        private String name;

        @Required
        private ResponseTool.ResponseToolType type;

        public static FunctionTool of(String name) {
            return new FunctionTool(name, ResponseTool.ResponseToolType.FUNCTION);
        }

    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor
    @Getter
    @ToString
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    class MCPTool {

        @Required
        private String serverLabel;

        private String name;

        @Required
        private HostedToolType type;

        public static MCPTool of(String serverLabel) {
            return new MCPTool(serverLabel, null, HostedToolType.MCP);
        }

        public static MCPTool of(String serverLabel, String name) {
            return new MCPTool(serverLabel, name, HostedToolType.MCP);
        }

    }

}
