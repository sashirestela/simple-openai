package io.github.sashirestela.openai.domain.assistant.v2;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.github.sashirestela.slimvalidator.constraints.Required;
import io.github.sashirestela.slimvalidator.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Singular;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Getter
@Builder
@JsonInclude(Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class VectorStoreRequest {

    @Singular
    @Size(max = 500)
    private List<String> fileIds;

    private String name;

    private ExpiresAfter expiresAfter;

    @Size(max = 16)
    private Map<String, String> metadata;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @ToString
    @Builder
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ExpiresAfter {

        @Required
        private Anchor anchor;

        @Required
        @Size(min = 1, max = 365)
        private Integer days;

        public enum Anchor {

            @JsonProperty("last_active_at")
            LAST_ACTIVE_AT;

        }

    }

}
