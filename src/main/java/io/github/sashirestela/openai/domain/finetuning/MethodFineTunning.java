package io.github.sashirestela.openai.domain.finetuning;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@JsonInclude(Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MethodFineTunning {

    private MethodType type;
    private Supervised supervised;
    private Dpo dpo;

    private MethodFineTunning(MethodType type, Supervised supervised, Dpo dpo) {
        this.type = type;
        this.supervised = supervised;
        this.dpo = dpo;
    }

    public static MethodFineTunning supervised(HyperParams hyperParameters) {
        return new MethodFineTunning(MethodType.SUPERVISED, new Supervised(hyperParameters), null);
    }

    public static MethodFineTunning dpo(HyperParams hyperParameters) {
        return new MethodFineTunning(MethodType.DPO, null, new Dpo(hyperParameters));
    }

    public enum MethodType {

        @JsonProperty("supervised")
        SUPERVISED,

        @JsonProperty("dpo")
        DPO;

    }

    @Getter
    @ToString
    @NoArgsConstructor
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Supervised {

        HyperParams hyperParameters;

        public Supervised(HyperParams hyperParameters) {
            this.hyperParameters = hyperParameters;
        }

    }

    @Getter
    @ToString
    @NoArgsConstructor
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Dpo {

        HyperParams hyperParameters;

        public Dpo(HyperParams hyperParameters) {
            this.hyperParameters = hyperParameters;
        }

    }

}
