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
        COMPUTER_USE_PREVIEW;

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

}
