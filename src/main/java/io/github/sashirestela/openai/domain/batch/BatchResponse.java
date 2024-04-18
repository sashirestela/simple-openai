package io.github.sashirestela.openai.domain.batch;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
@Getter
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BatchResponse {

    private String id;
    private String object;
    private BatchErrors errors;
    private String inputFileId;
    private String completionWindow;
    private String status;
    private String outputFileId;
    private String errorFileId;
    private Integer createdAt;
    private Integer inProgressAt;
    private Integer expiresAt;
    private Integer finalizingAt;
    private Integer completedAt;
    private Integer failedAt;
    private Integer expiredAt;
    private Integer cancellingAt;
    private Integer cancelledAt;
    private RequestCountsType requestCounts;
    private Map<String, String> metadata;

    @NoArgsConstructor
    @Getter
    @ToString
    public static class BatchErrors {

        private String object;
        private List<BatchError> data;

    }

    @NoArgsConstructor
    @Getter
    @ToString
    public static class BatchError {

        private String code;
        private String message;
        private String param;
        private Integer line;

    }

    @NoArgsConstructor
    @Getter
    @ToString
    public static class RequestCountsType {

        private Integer total;
        private Integer completed;
        private Integer failed;

    }

}
