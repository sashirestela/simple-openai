package io.github.sashirestela.openai.domain.moderation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@NoArgsConstructor
@Getter
@ToString
public class Moderation {

    private String id;
    private String model;
    private List<ModerationResult> results;

    @NoArgsConstructor
    @Getter
    @ToString
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ModerationResult {

        private Boolean flagged;
        private Category categories;
        private CategoryScore categoryScores;
        private CategoryAppliedInputType categoryAppliedInputTypes;

        @NoArgsConstructor
        @Getter
        @ToString
        public static class Category {

            private Boolean harassment;

            @JsonProperty("harassment/threatening")
            private Boolean harassmentThreatening;

            private Boolean hate;

            @JsonProperty("hate/threatening")
            private Boolean hateThreatening;

            private Boolean illicit;

            @JsonProperty("illicit/violent")
            private Boolean illicitViolent;

            @JsonProperty("self-harm")
            private Boolean selfHarm;

            @JsonProperty("self-harm/instructions")
            private Boolean selfHarmInstructions;

            @JsonProperty("self-harm/intent")
            private Boolean selfHarmIntent;

            private Boolean sexual;

            @JsonProperty("sexual/minors")
            private Boolean sexualMinors;

            private Boolean violence;

            @JsonProperty("violence/graphic")
            private Boolean violenceGraphic;

        }

        @NoArgsConstructor
        @Getter
        @ToString
        public static class CategoryScore {

            private Double harassment;

            @JsonProperty("harassment/threatening")
            private Double harassmentThreatening;

            private Double hate;

            @JsonProperty("hate/threatening")
            private Double hateThreatening;

            private Double illicit;

            @JsonProperty("illicit/violent")
            private Double illicitViolent;

            @JsonProperty("self-harm")
            private Double selfHarm;

            @JsonProperty("self-harm/instructions")
            private Double selfHarmInstructions;

            @JsonProperty("self-harm/intent")
            private Double selfHarmIntent;

            private Double sexual;

            @JsonProperty("sexual/minors")
            private Double sexualMinors;

            private Double violence;

            @JsonProperty("violence/graphic")
            private Double violenceGraphic;

        }

        @NoArgsConstructor
        @Getter
        @ToString
        public static class CategoryAppliedInputType {

            private List<String> harassment;

            @JsonProperty("harassment/threatening")
            private List<String> harassmentThreatening;

            private List<String> hate;

            @JsonProperty("hate/threatening")
            private List<String> hateThreatening;

            private List<String> illicit;

            @JsonProperty("illicit/violent")
            private List<String> illicitViolent;

            @JsonProperty("self-harm")
            private List<String> selfHarm;

            @JsonProperty("self-harm/instructions")
            private List<String> selfHarmInstructions;

            @JsonProperty("self-harm/intent")
            private List<String> selfHarmIntent;

            private List<String> sexual;

            @JsonProperty("sexual/minors")
            private List<String> sexualMinors;

            private List<String> violence;

            @JsonProperty("violence/graphic")
            private List<String> violenceGraphic;

        }

    }

}
