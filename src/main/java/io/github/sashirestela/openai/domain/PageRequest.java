package io.github.sashirestela.openai.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PageRequest {

    private Integer limit;
    private Order order;
    private String after;
    private String before;

    public enum Order {

        @JsonProperty("asc")
        ASC,

        @JsonProperty("desc")
        DESC

    }

}
