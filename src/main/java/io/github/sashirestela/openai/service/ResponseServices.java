package io.github.sashirestela.openai.service;

import io.github.sashirestela.openai.OpenAI;

/**
 * Services to use the OpenAI Response API.
 */
public interface ResponseServices {

    /**
     * Returns the Response service.
     * 
     * @return The Response service.
     */
    OpenAI.Responses responses();

}
