package io.github.sashirestela.openai.domain.response.events;

import io.github.sashirestela.cleverclient.annotation.StreamType;
import io.github.sashirestela.openai.domain.response.Response;
import io.github.sashirestela.openai.exception.OpenAIResponseInfo.OpenAIErrorResponse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static io.github.sashirestela.openai.domain.response.events.ResponseEventName.ERROR;
import static io.github.sashirestela.openai.domain.response.events.ResponseEventName.RESPONSE_COMPLETED;
import static io.github.sashirestela.openai.domain.response.events.ResponseEventName.RESPONSE_CONTENT_PART_ADDED;
import static io.github.sashirestela.openai.domain.response.events.ResponseEventName.RESPONSE_CONTENT_PART_DONE;
import static io.github.sashirestela.openai.domain.response.events.ResponseEventName.RESPONSE_CREATED;
import static io.github.sashirestela.openai.domain.response.events.ResponseEventName.RESPONSE_IN_PROGRESS;
import static io.github.sashirestela.openai.domain.response.events.ResponseEventName.RESPONSE_OUTPUT_ITEM_ADDED;
import static io.github.sashirestela.openai.domain.response.events.ResponseEventName.RESPONSE_OUTPUT_ITEM_DONE;
import static io.github.sashirestela.openai.domain.response.events.ResponseEventName.RESPONSE_OUTPUT_TEXT_DELTA;
import static io.github.sashirestela.openai.domain.response.events.ResponseEventName.RESPONSE_OUTPUT_TEXT_DONE;

/**
 * Annotation for methods that stream Response API events.
 */
@StreamType(type = Response.class, events = {
        RESPONSE_CREATED,
        RESPONSE_IN_PROGRESS,
        RESPONSE_COMPLETED
})
@StreamType(type = ResponseOutputItem.class, events = {
        RESPONSE_OUTPUT_ITEM_ADDED,
        RESPONSE_OUTPUT_ITEM_DONE
})
@StreamType(type = ResponseContentPart.class, events = {
        RESPONSE_CONTENT_PART_ADDED,
        RESPONSE_CONTENT_PART_DONE
})
@StreamType(type = ResponseDelta.class, events = {
        RESPONSE_OUTPUT_TEXT_DELTA
})
@StreamType(type = ResponseOutputText.class, events = {
        RESPONSE_OUTPUT_TEXT_DONE
})
@StreamType(type = OpenAIErrorResponse.class, events = { ERROR })
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ResponseStreamEvent {
}
