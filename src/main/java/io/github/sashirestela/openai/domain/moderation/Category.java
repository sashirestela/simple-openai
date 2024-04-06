package io.github.sashirestela.openai.domain.moderation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
public class Category {

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
