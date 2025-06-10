package io.github.sashirestela.openai.domain.image;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Size {
    // Common
    @JsonProperty("1024x1024")
    X_1024_1024,

    // DALL-E-2
    @JsonProperty("256x256")
    X_256_256,
    @JsonProperty("512x512")
    X_512_512,

    // DALL-E-3
    @JsonProperty("1792x1024")
    X_1792_1024,
    @JsonProperty("1024x1792")
    X_1024_1792,

    // GPT-Image-1
    @JsonProperty("1536x1024")
    X_1536_1024,
    @JsonProperty("1024x1536")
    X_1024_1536,
    @JsonProperty("auto")
    AUTO;

}
