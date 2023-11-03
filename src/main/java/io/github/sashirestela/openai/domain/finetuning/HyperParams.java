package io.github.sashirestela.openai.domain.finetuning;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class HyperParams {

    @JsonProperty("n_epochs")
    private Integer numberEpochs;

}