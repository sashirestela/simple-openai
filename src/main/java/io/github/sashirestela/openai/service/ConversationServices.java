package io.github.sashirestela.openai.service;

import io.github.sashirestela.openai.OpenAI;

public interface ConversationServices {

    OpenAI.Conversations conversations();

    OpenAI.ConversationItems conversationItems();

}
