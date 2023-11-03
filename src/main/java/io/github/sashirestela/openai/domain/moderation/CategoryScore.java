package io.github.sashirestela.openai.domain.moderation;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

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