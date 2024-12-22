package io.github.sashirestela.openai.domain.realtime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
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
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Response {

    private String id;
    private String object;
    private String status;
    private StatusDetails statusDetails;
    private List<Item> output;
    private Map<String, String> metadata;
    private UsageResponse usage;

    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class StatusDetails {

        private String type;
        private String reason;
        private ErrorDetail error;

    }

    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ErrorDetail {

        private String type;
        private String code;

    }

    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class UsageResponse {

        private Integer totalTokens;
        private Integer inputTokens;
        private Integer outputTokens;
        private TokenDetails inputTokenDetails;
        private TokenDetails outputTokenDetails;

    }

    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class TokenDetails {

        private Integer textTokens;
        private Integer audioTokens;
        private Integer cachedTokens;

    }

}
