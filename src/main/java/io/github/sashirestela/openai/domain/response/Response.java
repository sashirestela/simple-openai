package io.github.sashirestela.openai.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Response {

    private String id;
    private String model;
    private String object;
    
    @JsonProperty("created_at")
    private Long created;
    
    private String instructions;
    private List<Object> items;
    private Map<String, Object> metadata;
    
    private List<Object> output;
    
    @JsonProperty("previous_response_id")
    private String previousResponseId;
    
    private String user;
    private Usage usage;
    private Error error;
    
    @JsonProperty("incomplete_details")
    private IncompleteDetails incompleteDetails;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @JsonInclude(Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Usage {

        private Integer completionTokens;
        private Integer promptTokens;
        private Integer totalTokens;

    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @JsonInclude(Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Error {

        private String code;
        private String message;
        private String param;
        private String type;

    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @JsonInclude(Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class IncompleteDetails {

        private String reason;

        public enum Reason {
            @JsonProperty("content_filter")
            CONTENT_FILTER,

            @JsonProperty("max_tokens")
            MAX_TOKENS,

            @JsonProperty("token_limit")
            TOKEN_LIMIT
        }

    }

}
