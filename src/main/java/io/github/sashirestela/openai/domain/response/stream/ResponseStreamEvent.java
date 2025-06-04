package io.github.sashirestela.openai.domain.response.stream;

import io.github.sashirestela.cleverclient.annotation.StreamType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@StreamType(type = ResponseEvent.class,
        events = { EventName.RESPONSE_CREATED, EventName.RESPONSE_IN_PROGRESS, EventName.RESPONSE_COMPLETED,
                EventName.RESPONSE_FAILED, EventName.RESPONSE_INCOMPLETE, EventName.RESPONSE_QUEUED })
@StreamType(type = ResponseOutputItemEvent.class,
        events = { EventName.RESPONSE_OUTPUT_ITEM_ADDED, EventName.RESPONSE_OUTPUT_ITEM_DONE })
@StreamType(type = ResponseContentPartEvent.class,
        events = { EventName.RESPONSE_CONTENT_PART_ADDED, EventName.RESPONSE_CONTENT_PART_DONE })
@StreamType(type = ResponseOutputTextAnnotEvent.class,
        events = { EventName.RESPONSE_OUTPUT_TEXT_ANNOTATION_ADDED })
@StreamType(type = ResponseOutputTextEvent.class,
        events = { EventName.RESPONSE_OUTPUT_TEXT_DELTA, EventName.RESPONSE_OUTPUT_TEXT_DONE })
@StreamType(type = ResponseRefusalEvent.class,
        events = { EventName.RESPONSE_REFUSAL_DELTA, EventName.RESPONSE_REFUSAL_DONE })
@StreamType(type = ResponseFunctionCallArgsEvent.class,
        events = { EventName.RESPONSE_FUNCTION_CALL_ARGUMENTS_DELTA, EventName.RESPONSE_FUNCTION_CALL_ARGUMENTS_DONE })
@StreamType(type = ResponseFileSearchCallEvent.class,
        events = { EventName.RESPONSE_FILE_SEARCH_CALL_IN_PROGRESS, EventName.RESPONSE_FILE_SEARCH_CALL_SEARCHING,
                EventName.RESPONSE_FILE_SEARCH_CALL_COMPLETED })
@StreamType(type = ResponseWebSearchCallEvent.class,
        events = { EventName.RESPONSE_WEB_SEARCH_CALL_IN_PROGRESS, EventName.RESPONSE_WEB_SEARCH_CALL_SEARCHING,
                EventName.RESPONSE_WEB_SEARCH_CALL_COMPLETED })
@StreamType(type = ResponseReasonSummPartEvent.class,
        events = { EventName.RESPONSE_REASONING_SUMMARY_PART_ADDED, EventName.RESPONSE_REASONING_SUMMARY_PART_DONE })
@StreamType(type = ResponseReasonSummTextEvent.class,
        events = { EventName.RESPONSE_REASONING_SUMMARY_TEXT_DELTA, EventName.RESPONSE_REASONING_SUMMARY_TEXT_DONE })
@StreamType(type = ResponseImageGenerationCallEvent.class,
        events = { EventName.RESPONSE_IMAGE_GENERATION_CALL_COMPLETED,
                EventName.RESPONSE_IMAGE_GENERATION_CALL_GENERATING,
                EventName.RESPONSE_IMAGE_GENERATION_CALL_IN_PROGRESS })
@StreamType(type = ResponseImageGenerationCallPartialEvent.class,
        events = { EventName.RESPONSE_IMAGE_GENERATION_CALL_PARTIAL_IMAGE })
@StreamType(type = ResponseMcpCallArgumentsEvent.class,
        events = { EventName.RESPONSE_MCP_CALL_ARGUMENTS_DELTA, EventName.RESPONSE_MCP_CALL_ARGUMENTS_DONE })
@StreamType(type = ResponseMcpCallEvent.class,
        events = { EventName.RESPONSE_MCP_CALL_COMPLETED, EventName.RESPONSE_MCP_CALL_FAILED })
@StreamType(type = ResponseMcpCallInProgressEvent.class,
        events = { EventName.RESPONSE_MCP_CALL_IN_PROGRESS })
@StreamType(type = ResponseMcpListToolsEvent.class,
        events = { EventName.RESPONSE_MCP_LIST_TOOLS_COMPLETED, EventName.RESPONSE_MCP_LIST_TOOLS_FAILED,
                EventName.RESPONSE_MCP_LIST_TOOLS_IN_PROGRESS })
@StreamType(type = ResponseReasoningEvent.class,
        events = { EventName.RESPONSE_REASONING_DELTA, EventName.RESPONSE_REASONING_DONE })
@StreamType(type = ResponseReasoningSummaryEvent.class,
        events = { EventName.RESPONSE_REASONING_SUMMARY_DELTA, EventName.RESPONSE_REASONING_SUMMARY_DONE })
@StreamType(type = ResponseErrorEvent.class,
        events = { EventName.RESPONSE_ERROR })
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ResponseStreamEvent {
}
