package io.github.sashirestela.openai.domain.conversation;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.github.sashirestela.openai.domain.response.Input;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * A single item within a conversation. The set of possible types are the same as the output type of
 * a Response object.
 *
 * @see <a href=
 *      "https://developers.openai.com/api/reference/resources/conversations/subresources/items/methods/retrieve">Retrieve
 *      an item</a>
 */
@AllArgsConstructor
@Getter
@ToString
@JsonDeserialize(using = ConversationItemDeserializer.class)
public class ConversationItem {

    private Input.Item item;

}
