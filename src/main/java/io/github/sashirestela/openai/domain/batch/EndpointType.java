package io.github.sashirestela.openai.domain.batch;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum EndpointType {

    @JsonProperty("/v1/chat/completions")
    CHAT_COMPLETIONS,

    @JsonProperty("/v1/embeddings")
    EMBEDDINGS;

}
