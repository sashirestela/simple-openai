package io.github.sashirestela.openai.domain.response.stream;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.github.sashirestela.openai.domain.response.Input;
import io.github.sashirestela.openai.domain.response.ItemDeserializer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResponseOutputItemEvent {

    private String type;
    private Integer outputIndex;
    @JsonDeserialize(using = ItemDeserializer.class)
    private Input.Item item;

}
