package io.github.sashirestela.openai.domain.moderation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ModerationResult {

    private Boolean flagged;
    private Category categories;
    private CategoryScore categoryScores;

    @NoArgsConstructor
    @Getter
    @ToString
    public static class Category {

        private Boolean sexual;

        private Boolean hate;

        private Boolean harassment;

        @JsonProperty("self-harm")
        private Boolean selfHarm;

        @JsonProperty("sexual/minors")
        private Boolean sexualMinors;

        @JsonProperty("hate/threatening")
        private Boolean hateThreatening;

        @JsonProperty("violence/graphic")
        private Boolean violencGraphic;

        @JsonProperty("self-harm/intent")
        private Boolean selfHarmIntent;

        @JsonProperty("self-harm/instructions")
        private Boolean selfHarmInstructions;

        @JsonProperty("harassment/threatening")
        private Boolean harassmentThreatening;

        private Boolean violence;

    }

    @NoArgsConstructor
    @Getter
    @ToString
    public class CategoryScore {

        private Double sexual;

        private Double hate;

        private Double harassment;

        @JsonProperty("self-harm")
        private Double selfHarm;

        @JsonProperty("sexual/minors")
        private Double sexualMinors;

        @JsonProperty("hate/threatening")
        private Double hateThreatening;

        @JsonProperty("violence/graphic")
        private Double violencGraphic;

        @JsonProperty("self-harm/intent")
        private Double selfHarmIntent;

        @JsonProperty("self-harm/instructions")
        private Double selfHarmInstructions;

        @JsonProperty("harassment/threatening")
        private Double harassmentThreatening;

        private Double violence;

    }

}
