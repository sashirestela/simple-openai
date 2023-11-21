package io.github.sashirestela.openai.domain.file;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum PurposeType {

    @JsonProperty("fine-tune")
    FINE_TUNE,

    @JsonProperty("assistants")
    ASSISTANTS;
    
}