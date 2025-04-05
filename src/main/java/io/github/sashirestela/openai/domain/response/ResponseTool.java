package io.github.sashirestela.openai.domain.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseTool {

    public enum ResponseToolType {

        @JsonProperty("file_search")
        FILE_SEARCH,

        @JsonProperty("function")
        FUNCTION,

        @JsonProperty("computer_use_preview")
        COMPUTER_USE_PREVIEW,

        @JsonProperty("web_search_preview")
        WEB_SEARCH_PREVIEW;

    }

}
