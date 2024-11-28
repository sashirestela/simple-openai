package io.github.sashirestela.openai.domain.realtime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sashirestela.cleverclient.support.Configurator;
import io.github.sashirestela.cleverclient.util.JsonUtil;
import io.github.sashirestela.openai.common.function.FunctionDef;
import io.github.sashirestela.openai.common.function.Functional;
import io.github.sashirestela.openai.domain.chat.ChatRequest.Modality;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BaseEventTest {

    static Configuration configuration;
    static Item item;
    static Response response;
    static ServerEvent.ErrorDetail errorDetail;
    static ServerEvent.Conversation conversation;
    static ServerEvent.Part part;
    static ServerEvent.RateLimit rateLimit;

    @BeforeAll
    static void setup() {
        Configurator.builder().objectMapper(new ObjectMapper()).build();

        configuration = Configuration.builder()
                .modality(Modality.TEXT)
                .modality(Modality.AUDIO)
                .instructions("instructions")
                .voice(Configuration.VoiceRealtime.BALLAD)
                .inputAudioFormat(Configuration.AudioFormatRealtime.PCM16)
                .outputAudioFormat(Configuration.AudioFormatRealtime.PCM16)
                .inputAudioTranscription(Configuration.InputAudioTranscription.of("model"))
                .turnDetection(Configuration.TurnDetection.builder()
                        .threshold(0.5)
                        .prefixPaddingMs(100)
                        .silenceDurationMs(100)
                        .build())
                .tool(Configuration.ToolRealtime.of(FunctionDef.builder()
                        .name("DemoFunction")
                        .description("A demo function")
                        .functionalClass(DemoFunction.class)
                        .build()))
                .toolChoice(null)
                .temperature(0.9)
                .maxResponseOutputTokens(2048)
                .build();

        item = Item.builder()
                .id("id")
                .type(Item.ItemType.FUNCTION_CALL)
                .status("complete")
                .role(Item.RoleItemMessage.USER)
                .contentItem(Item.ContentItem.builder()
                        .type(Item.ContentItemType.INPUT_TEXT)
                        .text("text")
                        .audio("audio")
                        .transcript("transcript")
                        .build())
                .callId("callId")
                .name("name")
                .arguments("arguments")
                .output("output")
                .build();

        response = Response.builder()
                .id("id")
                .object("object")
                .status("status")
                .statusDetails(Response.StatusDetails.builder()
                        .type("type")
                        .reason("reason")
                        .error(Response.ErrorDetail.builder()
                                .type("type")
                                .code("code")
                                .build())
                        .build())
                .output(List.of(item, item))
                .usage(Response.UsageResponse.builder()
                        .totalTokens(1000)
                        .inputTokens(1000)
                        .outputTokens(1000)
                        .inputTokenDetails(Response.TokenDetails.builder()
                                .cachedTokens(1000)
                                .textTokens(1000)
                                .audioTokens(1000)
                                .build())
                        .outputTokenDetails(Response.TokenDetails.builder()
                                .textTokens(1000)
                                .audioTokens(1000)
                                .build())
                        .build())
                .build();

        errorDetail = ServerEvent.ErrorDetail.builder()
                .type("type")
                .code("code")
                .message("message")
                .param("param")
                .eventId("eventId")
                .build();

        conversation = ServerEvent.Conversation.builder()
                .id("id")
                .object("object")
                .build();

        part = ServerEvent.Part.builder()
                .type("type")
                .text("text")
                .audio("audio")
                .transcript("transcript")
                .build();

        rateLimit = ServerEvent.RateLimit.builder()
                .name("name")
                .limit(100)
                .remaining(200)
                .resetSeconds(127.5)
                .build();
    }

    @Test
    void testSerializationDeserializationClientEventClasses() {
        var sessionUpdate = ClientEvent.SessionUpdate.of("eventId", configuration);
        var newSessionUpdate = JsonUtil.jsonToObject(JsonUtil.objectToJson(sessionUpdate),
                ClientEvent.SessionUpdate.class);
        assertEquals(sessionUpdate.toString(), newSessionUpdate.toString());

        sessionUpdate = ClientEvent.SessionUpdate.of(configuration);
        newSessionUpdate = JsonUtil.jsonToObject(JsonUtil.objectToJson(sessionUpdate),
                ClientEvent.SessionUpdate.class);
        assertEquals(sessionUpdate.toString(), newSessionUpdate.toString());

        var inputAudioBufferAppend = ClientEvent.InputAudioBufferAppend.of("eventId", "mock_audio_in_base64_format");
        var newInputAudioBufferAppend = JsonUtil.jsonToObject(JsonUtil.objectToJson(inputAudioBufferAppend),
                ClientEvent.InputAudioBufferAppend.class);
        assertEquals(inputAudioBufferAppend.toString(), newInputAudioBufferAppend.toString());

        inputAudioBufferAppend = ClientEvent.InputAudioBufferAppend.of("mock_audio_in_base64_format");
        newInputAudioBufferAppend = JsonUtil.jsonToObject(JsonUtil.objectToJson(inputAudioBufferAppend),
                ClientEvent.InputAudioBufferAppend.class);
        assertEquals(inputAudioBufferAppend.toString(), newInputAudioBufferAppend.toString());

        var inputAudioBufferCommit = ClientEvent.InputAudioBufferCommit.of("eventId");
        var newInputAudioBufferCommit = JsonUtil.jsonToObject(JsonUtil.objectToJson(inputAudioBufferCommit),
                ClientEvent.InputAudioBufferCommit.class);
        assertEquals(inputAudioBufferCommit.toString(), newInputAudioBufferCommit.toString());

        inputAudioBufferCommit = ClientEvent.InputAudioBufferCommit.of();
        newInputAudioBufferCommit = JsonUtil.jsonToObject(JsonUtil.objectToJson(inputAudioBufferCommit),
                ClientEvent.InputAudioBufferCommit.class);
        assertEquals(inputAudioBufferCommit.toString(), newInputAudioBufferCommit.toString());

        var inputAudioBufferClear = ClientEvent.InputAudioBufferClear.of("eventId");
        var newInputAudioBufferClear = JsonUtil.jsonToObject(JsonUtil.objectToJson(inputAudioBufferClear),
                ClientEvent.InputAudioBufferClear.class);
        assertEquals(inputAudioBufferClear.toString(), newInputAudioBufferClear.toString());

        inputAudioBufferClear = ClientEvent.InputAudioBufferClear.of();
        newInputAudioBufferClear = JsonUtil.jsonToObject(JsonUtil.objectToJson(inputAudioBufferClear),
                ClientEvent.InputAudioBufferClear.class);
        assertEquals(inputAudioBufferClear.toString(), newInputAudioBufferClear.toString());

        var conversationItemCreate = ClientEvent.ConversationItemCreate.of("eventId", "prevEventId", item);
        var newConversationItemCreate = JsonUtil.jsonToObject(JsonUtil.objectToJson(conversationItemCreate),
                ClientEvent.ConversationItemCreate.class);
        assertEquals(conversationItemCreate.toString(), newConversationItemCreate.toString());

        conversationItemCreate = ClientEvent.ConversationItemCreate.of("prevEventId", item);
        newConversationItemCreate = JsonUtil.jsonToObject(JsonUtil.objectToJson(conversationItemCreate),
                ClientEvent.ConversationItemCreate.class);
        assertEquals(conversationItemCreate.toString(), newConversationItemCreate.toString());

        conversationItemCreate = ClientEvent.ConversationItemCreate.of(item);
        newConversationItemCreate = JsonUtil.jsonToObject(JsonUtil.objectToJson(conversationItemCreate),
                ClientEvent.ConversationItemCreate.class);
        assertEquals(conversationItemCreate.toString(), newConversationItemCreate.toString());

        var conversationItemTruncate = ClientEvent.ConversationItemTruncate.of("eventId", "itemId", 0, 360);
        var newConversationItemTruncate = JsonUtil.jsonToObject(JsonUtil.objectToJson(conversationItemTruncate),
                ClientEvent.ConversationItemTruncate.class);
        assertEquals(conversationItemTruncate.toString(), newConversationItemTruncate.toString());

        conversationItemTruncate = ClientEvent.ConversationItemTruncate.of("itemId", 0, 360);
        newConversationItemTruncate = JsonUtil.jsonToObject(JsonUtil.objectToJson(conversationItemTruncate),
                ClientEvent.ConversationItemTruncate.class);
        assertEquals(conversationItemTruncate.toString(), newConversationItemTruncate.toString());

        var conversationItemDelete = ClientEvent.ConversationItemDelete.of("eventId", "itemId");
        var newConversationItemDelete = JsonUtil.jsonToObject(JsonUtil.objectToJson(conversationItemDelete),
                ClientEvent.ConversationItemDelete.class);
        assertEquals(conversationItemDelete.toString(), newConversationItemDelete.toString());

        conversationItemDelete = ClientEvent.ConversationItemDelete.of("itemId");
        newConversationItemDelete = JsonUtil.jsonToObject(JsonUtil.objectToJson(conversationItemDelete),
                ClientEvent.ConversationItemDelete.class);
        assertEquals(conversationItemDelete.toString(), newConversationItemDelete.toString());

        var responseCreate = ClientEvent.ResponseCreate.of("eventId", configuration);
        var newResponseCreate = JsonUtil.jsonToObject(JsonUtil.objectToJson(responseCreate),
                ClientEvent.ResponseCreate.class);
        assertEquals(responseCreate.toString(), newResponseCreate.toString());

        responseCreate = ClientEvent.ResponseCreate.of(configuration);
        newResponseCreate = JsonUtil.jsonToObject(JsonUtil.objectToJson(responseCreate),
                ClientEvent.ResponseCreate.class);
        assertEquals(responseCreate.toString(), newResponseCreate.toString());

        var responseCancel = ClientEvent.ResponseCancel.of("eventId");
        var newResponseCancel = JsonUtil.jsonToObject(JsonUtil.objectToJson(responseCancel),
                ClientEvent.ResponseCancel.class);
        assertEquals(responseCancel.toString(), newResponseCancel.toString());

        responseCancel = ClientEvent.ResponseCancel.of();
        newResponseCancel = JsonUtil.jsonToObject(JsonUtil.objectToJson(responseCancel),
                ClientEvent.ResponseCancel.class);
        assertEquals(responseCancel.toString(), newResponseCancel.toString());
    }

    @Test
    void testSerializationDeserializationServerEventClasses() {
        var error = ServerEvent.ErrorEvent.builder()
                .type(Realtime.ERROR)
                .error(errorDetail)
                .build();
        var newError = JsonUtil.jsonToObject(JsonUtil.objectToJson(error), ServerEvent.ErrorEvent.class);
        assertEquals(error.toString(), newError.toString());

        var sessionCreated = ServerEvent.SessionCreated.builder()
                .type(Realtime.SESSION_CREATED)
                .session(configuration)
                .build();
        var newSessionCreated = JsonUtil.jsonToObject(JsonUtil.objectToJson(sessionCreated),
                ServerEvent.SessionCreated.class);
        assertEquals(sessionCreated.toString(), newSessionCreated.toString());

        var sessionUpdated = ServerEvent.SessionUpdated.builder()
                .type(Realtime.SESSION_UPDATED)
                .session(configuration)
                .build();
        var newSessionUpdated = JsonUtil.jsonToObject(JsonUtil.objectToJson(sessionUpdated),
                ServerEvent.SessionUpdated.class);
        assertEquals(sessionUpdated.toString(), newSessionUpdated.toString());

        var conversationCreated = ServerEvent.ConversationCreated.builder()
                .type(Realtime.CONVERSATION_CREATED)
                .conversation(conversation)
                .build();
        var newConversationCreated = JsonUtil.jsonToObject(JsonUtil.objectToJson(conversationCreated),
                ServerEvent.ConversationCreated.class);
        assertEquals(conversationCreated.toString(), newConversationCreated.toString());

        var conversationItemCreated = ServerEvent.ConversationItemCreated.builder()
                .type(Realtime.CONVERSATION_ITEM_CREATED)
                .previousItemId("prevItemId")
                .item(item)
                .build();
        var newConversationItemCreated = JsonUtil.jsonToObject(JsonUtil.objectToJson(conversationItemCreated),
                ServerEvent.ConversationItemCreated.class);
        assertEquals(conversationItemCreated.toString(), newConversationItemCreated.toString());

        var conversationItemAudioTransCompleted = ServerEvent.ConversationItemAudioTransCompleted.builder()
                .type(Realtime.CONVERSATION_ITEM_AUDIO_TRANS_COMPLETED)
                .itemId("itemId")
                .contentIndex(0)
                .transcript("transcript")
                .build();
        var newConversationItemAudioTransCompleted = JsonUtil.jsonToObject(
                JsonUtil.objectToJson(conversationItemAudioTransCompleted),
                ServerEvent.ConversationItemAudioTransCompleted.class);
        assertEquals(conversationItemAudioTransCompleted.toString(), newConversationItemAudioTransCompleted.toString());

        var conversationItemAudioTransFailed = ServerEvent.ConversationItemAudioTransFailed.builder()
                .type(Realtime.CONVERSATION_ITEM_AUDIO_TRANS_FAILED)
                .itemId("itemId")
                .contentIndex(0)
                .error(errorDetail)
                .build();
        var newConversationItemAudioTransFailed = JsonUtil.jsonToObject(
                JsonUtil.objectToJson(conversationItemAudioTransFailed),
                ServerEvent.ConversationItemAudioTransFailed.class);
        assertEquals(conversationItemAudioTransFailed.toString(), newConversationItemAudioTransFailed.toString());

        var conversationItemTruncated = ServerEvent.ConversationItemTruncated.builder()
                .type(Realtime.CONVERSATION_ITEM_TRUNCATED)
                .itemId("itemId")
                .contentIndex(0)
                .audioEndMs(1000)
                .build();
        var newConversationItemTruncated = JsonUtil.jsonToObject(JsonUtil.objectToJson(conversationItemTruncated),
                ServerEvent.ConversationItemTruncated.class);
        assertEquals(conversationItemTruncated.toString(), newConversationItemTruncated.toString());

        var conversationItemDeleted = ServerEvent.ConversationItemDeleted.builder()
                .type(Realtime.CONVERSATION_ITEM_DELETED)
                .itemId("itemId")
                .build();
        var newConversationItemDeleted = JsonUtil.jsonToObject(JsonUtil.objectToJson(conversationItemDeleted),
                ServerEvent.ConversationItemDeleted.class);
        assertEquals(conversationItemDeleted.toString(), newConversationItemDeleted.toString());

        var inputAudioBufferCommitted = ServerEvent.InputAudioBufferCommitted.builder()
                .type(Realtime.INPUT_AUDIO_BUFFER_COMMITTED)
                .previousItemId("prevItemId")
                .itemId("itemId")
                .build();
        var newInputAudioBufferCommitted = JsonUtil.jsonToObject(JsonUtil.objectToJson(inputAudioBufferCommitted),
                ServerEvent.InputAudioBufferCommitted.class);
        assertEquals(inputAudioBufferCommitted.toString(), newInputAudioBufferCommitted.toString());

        var inputAudioBufferCleared = ServerEvent.InputAudioBufferCleared.builder()
                .type(Realtime.INPUT_AUDIO_BUFFER_CLEARED)
                .build();
        var newInputAudioBufferCleared = JsonUtil.jsonToObject(JsonUtil.objectToJson(inputAudioBufferCleared),
                ServerEvent.InputAudioBufferCleared.class);
        assertEquals(inputAudioBufferCleared.toString(), newInputAudioBufferCleared.toString());

        var inputAudioBufferSpeechStarted = ServerEvent.InputAudioBufferSpeechStarted.builder()
                .type(Realtime.INPUT_AUDIO_BUFFER_SPEECH_STARTED)
                .audioStartMs(1000)
                .itemId("itemId")
                .build();
        var newInputAudioBufferSpeechStarted = JsonUtil.jsonToObject(
                JsonUtil.objectToJson(inputAudioBufferSpeechStarted),
                ServerEvent.InputAudioBufferSpeechStarted.class);
        assertEquals(inputAudioBufferSpeechStarted.toString(), newInputAudioBufferSpeechStarted.toString());

        var inputAudioBufferSpeechStopped = ServerEvent.InputAudioBufferSpeechStopped.builder()
                .type(Realtime.INPUT_AUDIO_BUFFER_SPEECH_STOPPED)
                .audioEndMs(1000)
                .itemId("itemId")
                .build();
        var newInputAudioBufferSpeechStopped = JsonUtil.jsonToObject(
                JsonUtil.objectToJson(inputAudioBufferSpeechStopped),
                ServerEvent.InputAudioBufferSpeechStopped.class);
        assertEquals(inputAudioBufferSpeechStopped.toString(), newInputAudioBufferSpeechStopped.toString());

        var rateLimitsUpdated = ServerEvent.RateLimitsUpdated.builder()
                .type(Realtime.RATE_LIMITS_UPDATED)
                .rateLimits(List.of(rateLimit, rateLimit))
                .build();
        var newRateLimitsUpdated = JsonUtil.jsonToObject(JsonUtil.objectToJson(rateLimitsUpdated),
                ServerEvent.RateLimitsUpdated.class);
        assertEquals(rateLimitsUpdated.toString(), newRateLimitsUpdated.toString());
    }

    @Test
    void testSerializationDeserializationServerEventResponseClasses() {
        var responseCreated = ServerEvent.ResponseCreated.builder()
                .type(Realtime.RESPONSE_CREATED)
                .response(response)
                .build();
        var newResponseCreated = JsonUtil.jsonToObject(JsonUtil.objectToJson(responseCreated),
                ServerEvent.ResponseCreated.class);
        assertEquals(responseCreated.toString(), newResponseCreated.toString());

        var responseDone = ServerEvent.ResponseDone.builder()
                .type(Realtime.RESPONSE_DONE)
                .response(response)
                .build();
        var newResponseDone = JsonUtil.jsonToObject(JsonUtil.objectToJson(responseDone),
                ServerEvent.ResponseDone.class);
        assertEquals(responseDone.toString(), newResponseDone.toString());

        var responseOutputItemAdded = ServerEvent.ResponseOutputItemAdded.builder()
                .type(Realtime.RESPONSE_OUTPUT_ITEM_ADDED)
                .responseId("respId")
                .outputIndex(1)
                .item(item)
                .build();
        var newResponseOutputItemAdded = JsonUtil.jsonToObject(JsonUtil.objectToJson(responseOutputItemAdded),
                ServerEvent.ResponseOutputItemAdded.class);
        assertEquals(responseOutputItemAdded.toString(), newResponseOutputItemAdded.toString());

        var responseOutputItemDone = ServerEvent.ResponseOutputItemDone.builder()
                .type(Realtime.RESPONSE_OUTPUT_ITEM_DONE)
                .responseId("respId")
                .outputIndex(1)
                .item(item)
                .build();
        var newResponseOutputItemDone = JsonUtil.jsonToObject(JsonUtil.objectToJson(responseOutputItemDone),
                ServerEvent.ResponseOutputItemDone.class);
        assertEquals(responseOutputItemDone.toString(), newResponseOutputItemDone.toString());

        var responseContentPartAdded = ServerEvent.ResponseContentPartAdded.builder()
                .type(Realtime.RESPONSE_CONTENT_PART_ADDED)
                .responseId("respId")
                .itemId("itemId")
                .outputIndex(1)
                .contentIndex(1)
                .part(part)
                .build();
        var newResponseContentPartAdded = JsonUtil.jsonToObject(JsonUtil.objectToJson(responseContentPartAdded),
                ServerEvent.ResponseContentPartAdded.class);
        assertEquals(responseContentPartAdded.toString(), newResponseContentPartAdded.toString());

        var responseContentPartDone = ServerEvent.ResponseContentPartDone.builder()
                .type(Realtime.RESPONSE_CONTENT_PART_DONE)
                .responseId("respId")
                .itemId("itemId")
                .outputIndex(1)
                .contentIndex(1)
                .part(part)
                .build();
        var newResponseContentPartDone = JsonUtil.jsonToObject(JsonUtil.objectToJson(responseContentPartDone),
                ServerEvent.ResponseContentPartDone.class);
        assertEquals(responseContentPartDone.toString(), newResponseContentPartDone.toString());

        var responseTextDelta = ServerEvent.ResponseTextDelta.builder()
                .type(Realtime.RESPONSE_TEXT_DELTA)
                .responseId("respId")
                .itemId("itemId")
                .outputIndex(1)
                .contentIndex(1)
                .delta("delta")
                .build();
        var newResponseTextDelta = JsonUtil.jsonToObject(JsonUtil.objectToJson(responseTextDelta),
                ServerEvent.ResponseTextDelta.class);
        assertEquals(responseTextDelta.toString(), newResponseTextDelta.toString());

        var responseTextDone = ServerEvent.ResponseTextDone.builder()
                .type(Realtime.RESPONSE_TEXT_DONE)
                .responseId("respId")
                .itemId("itemId")
                .outputIndex(1)
                .contentIndex(1)
                .text("text")
                .build();
        var newResponseTextDone = JsonUtil.jsonToObject(JsonUtil.objectToJson(responseTextDone),
                ServerEvent.ResponseTextDone.class);
        assertEquals(responseTextDone.toString(), newResponseTextDone.toString());

        var responseAudioTranscriptDelta = ServerEvent.ResponseAudioTranscriptDelta.builder()
                .type(Realtime.RESPONSE_AUDIO_TRANSCRIPT_DELTA)
                .responseId("respId")
                .itemId("itemId")
                .outputIndex(1)
                .contentIndex(1)
                .delta("delta")
                .build();
        var newResponseAudioTranscriptDelta = JsonUtil.jsonToObject(JsonUtil.objectToJson(responseAudioTranscriptDelta),
                ServerEvent.ResponseAudioTranscriptDelta.class);
        assertEquals(responseAudioTranscriptDelta.toString(), newResponseAudioTranscriptDelta.toString());

        var responseAudioTranscriptDone = ServerEvent.ResponseAudioTranscriptDone.builder()
                .type(Realtime.RESPONSE_AUDIO_TRANSCRIPT_DONE)
                .responseId("respId")
                .itemId("itemId")
                .outputIndex(1)
                .contentIndex(1)
                .transcript("transcript")
                .build();
        var newResponseAudioTranscriptDone = JsonUtil.jsonToObject(JsonUtil.objectToJson(responseAudioTranscriptDone),
                ServerEvent.ResponseAudioTranscriptDone.class);
        assertEquals(responseAudioTranscriptDone.toString(), newResponseAudioTranscriptDone.toString());

        var responseAudioDelta = ServerEvent.ResponseAudioDelta.builder()
                .type(Realtime.RESPONSE_AUDIO_DELTA)
                .responseId("respId")
                .itemId("itemId")
                .outputIndex(1)
                .contentIndex(1)
                .delta("delta")
                .build();
        var newResponseAudioDelta = JsonUtil.jsonToObject(JsonUtil.objectToJson(responseAudioDelta),
                ServerEvent.ResponseAudioDelta.class);
        assertEquals(responseAudioDelta.toString(), newResponseAudioDelta.toString());

        var responseAudioDone = ServerEvent.ResponseAudioDone.builder()
                .type(Realtime.RESPONSE_AUDIO_DONE)
                .responseId("respId")
                .itemId("itemId")
                .outputIndex(1)
                .contentIndex(1)
                .build();
        var newResponseAudioDone = JsonUtil.jsonToObject(JsonUtil.objectToJson(responseAudioDone),
                ServerEvent.ResponseAudioDone.class);
        assertEquals(responseAudioDone.toString(), newResponseAudioDone.toString());

        var responseFunctionCallArgumentsDelta = ServerEvent.ResponseFunctionCallArgumentsDelta.builder()
                .type(Realtime.RESPONSE_FUNCTION_CALL_ARGS_DELTA)
                .responseId("respId")
                .itemId("itemId")
                .outputIndex(1)
                .callId("callId")
                .delta("delta")
                .build();
        var newResponseFunctionCallArgumentsDelta = JsonUtil.jsonToObject(
                JsonUtil.objectToJson(responseFunctionCallArgumentsDelta),
                ServerEvent.ResponseFunctionCallArgumentsDelta.class);
        assertEquals(responseFunctionCallArgumentsDelta.toString(), newResponseFunctionCallArgumentsDelta.toString());

        var responseFunctionCallArgumentsDone = ServerEvent.ResponseFunctionCallArgumentsDone.builder()
                .type(Realtime.RESPONSE_FUNCTION_CALL_ARGS_DONE)
                .responseId("respId")
                .itemId("itemId")
                .outputIndex(1)
                .callId("callId")
                .arguments("args")
                .build();
        var newResponseFunctionCallArgumentsDone = JsonUtil.jsonToObject(
                JsonUtil.objectToJson(responseFunctionCallArgumentsDone),
                ServerEvent.ResponseFunctionCallArgumentsDone.class);
        assertEquals(responseFunctionCallArgumentsDone.toString(), newResponseFunctionCallArgumentsDone.toString());
    }

    static class DemoFunction implements Functional {

        @JsonPropertyDescription("A numeric field")
        @JsonProperty(required = true)
        public Integer fieldInteger;

        @JsonPropertyDescription("A text field")
        @JsonProperty(required = true)
        public String fieldString;

        @Override
        public Object execute() {
            return null;
        }

    }

}
