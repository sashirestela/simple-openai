package io.github.sashirestela.openai.domain.conversation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.github.sashirestela.openai.domain.response.Input;
import io.github.sashirestela.slimvalidator.constraints.Required;
import io.github.sashirestela.slimvalidator.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.util.List;

@Getter
@Builder
@JsonInclude(Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ConversationItemsRequest {

    @Required
    @Size(max = 20)
    @Singular
    private List<Input> items;

}
