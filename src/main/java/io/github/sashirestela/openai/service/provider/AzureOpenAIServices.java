package io.github.sashirestela.openai.service.provider;

import io.github.sashirestela.openai.service.ChatCompletionServices;
import io.github.sashirestela.openai.service.FileServices;

public interface AzureOpenAIServices extends
        ChatCompletionServices,
        FileServices {
}
