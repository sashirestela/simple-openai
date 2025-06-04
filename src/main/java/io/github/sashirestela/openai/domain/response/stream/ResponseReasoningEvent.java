package io.github.sashirestela.openai.domain.response.stream;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResponseReasoningEvent {

    private String type;
    private String itemId;
    private Integer outputIndex;
    private Integer contentIndex;
    private Object delta;
    private String text;
    private Integer sequenceNumber;

}
