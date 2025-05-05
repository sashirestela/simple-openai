package io.github.sashirestela.openai.domain.response.events;

import io.github.sashirestela.openai.domain.response.Response;
import lombok.Getter;

/**
 * Base class for all Response API streaming events.
 */
@Getter
public class ResponseBaseEvent {

    /**
     * The event type.
     */
    private String type;

    /**
     * The response object containing the current state.
     */
    private Response response;

}
