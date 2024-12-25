package io.github.sashirestela.openai.service.provider;

import io.github.sashirestela.openai.service.AssistantServices;
import io.github.sashirestela.openai.service.AudioServices;
import io.github.sashirestela.openai.service.BatchServices;
import io.github.sashirestela.openai.service.ChatCompletionServices;
import io.github.sashirestela.openai.service.CompletionServices;
import io.github.sashirestela.openai.service.EmbeddingServices;
import io.github.sashirestela.openai.service.FileServices;
import io.github.sashirestela.openai.service.FineTunningServices;
import io.github.sashirestela.openai.service.ImageServices;
import io.github.sashirestela.openai.service.ModelServices;
import io.github.sashirestela.openai.service.ModerationServices;
import io.github.sashirestela.openai.service.RealtimeServices;
import io.github.sashirestela.openai.service.SessionServices;
import io.github.sashirestela.openai.service.UploadServices;

public interface StandardOpenAIServices extends
        AssistantServices,
        AudioServices,
        BatchServices,
        ChatCompletionServices,
        CompletionServices,
        EmbeddingServices,
        FileServices,
        FineTunningServices,
        ImageServices,
        ModelServices,
        ModerationServices,
        RealtimeServices,
        SessionServices,
        UploadServices {
}
