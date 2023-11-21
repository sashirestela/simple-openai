package io.github.sashirestela.openai.domain.image;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public abstract class AbstractImageRequest {

    @JsonInclude(Include.NON_NULL)
    protected String model;

    @JsonInclude(Include.NON_NULL)
    protected Integer n;

    @JsonInclude(Include.NON_NULL)
    protected Size size;

    @JsonInclude(Include.NON_NULL)
    @JsonProperty("response_format")
    protected ImageRespFmt responseFormat;

    @JsonInclude(Include.NON_NULL)
    protected String user;

}