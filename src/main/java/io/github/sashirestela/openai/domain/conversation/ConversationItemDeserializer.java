package io.github.sashirestela.openai.domain.conversation;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import io.github.sashirestela.openai.domain.response.ItemDeserializer;

import java.io.IOException;

public class ConversationItemDeserializer extends JsonDeserializer<ConversationItem> {

    private static final ItemDeserializer DELEGATE = new ItemDeserializer();

    @Override
    public ConversationItem deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        return new ConversationItem(DELEGATE.deserialize(jp, ctxt));
    }

}
