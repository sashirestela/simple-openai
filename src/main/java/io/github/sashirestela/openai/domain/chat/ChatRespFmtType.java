package io.github.sashirestela.openai.domain.chat;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ChatRespFmtType {

    @JsonProperty("text")
    TEXT,

    @JsonProperty("json_object")
    JSON;

}