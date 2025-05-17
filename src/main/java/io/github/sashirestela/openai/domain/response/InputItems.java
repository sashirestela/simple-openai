package io.github.sashirestela.openai.domain.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@NoArgsConstructor
@Getter
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class InputItems {

    private String object;
    @JsonDeserialize(contentUsing = ItemDeserializer.class)
    private List<Input.Item> data;
    private String firstId;
    private String lastId;
    private boolean hasMore;

}
