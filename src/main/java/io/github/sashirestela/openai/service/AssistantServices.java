package io.github.sashirestela.openai.service;

import io.github.sashirestela.openai.OpenAIBeta2;

public interface AssistantServices {

    OpenAIBeta2.Assistants assistants();

    OpenAIBeta2.Threads threads();

    OpenAIBeta2.ThreadMessages threadMessages();

    OpenAIBeta2.ThreadRuns threadRuns();

    OpenAIBeta2.ThreadRunSteps threadRunSteps();

    OpenAIBeta2.VectorStores vectorStores();

    OpenAIBeta2.VectorStoreFiles vectorStoreFiles();

    OpenAIBeta2.VectorStoreFileBatches vectorStoreFileBatches();

}
