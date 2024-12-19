package io.github.sashirestela.openai.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.Value;

import java.util.List;
import java.util.Map;

@Value
@Builder
public class OpenAIResponseInfo {

    private int status;
    private OpenAIErrorResponse errorResponse;
    private String httpMethod;
    private String url;
    private Map<String, List<String>> responseHeaders;
    private Map<String, List<String>> requestHeaders;

    @Getter
    @ToString
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OpenAIErrorResponse {

        private OpenAIError error;

        @Getter
        @ToString
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class OpenAIError {

            private String message;
            private String type;
            private String param;
            private String code;

        }

    }

}
