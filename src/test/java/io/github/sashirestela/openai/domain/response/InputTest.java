package io.github.sashirestela.openai.domain.response;

import io.github.sashirestela.cleverclient.util.JsonUtil;
import io.github.sashirestela.openai.domain.response.Action.ScreenshotAction;
import io.github.sashirestela.openai.domain.response.Input.Citation.FileCitation;
import io.github.sashirestela.openai.domain.response.Input.Citation.FilePath;
import io.github.sashirestela.openai.domain.response.Input.Citation.UrlCitation;
import io.github.sashirestela.openai.domain.response.Input.Content.FileInputContent;
import io.github.sashirestela.openai.domain.response.Input.Content.ImageInputContent;
import io.github.sashirestela.openai.domain.response.Input.Content.TextInputContent;
import io.github.sashirestela.openai.domain.response.Input.FileSearchResult;
import io.github.sashirestela.openai.domain.response.Input.InputMessage;
import io.github.sashirestela.openai.domain.response.Input.Item.ComputerCallItem;
import io.github.sashirestela.openai.domain.response.Input.Item.ComputerCallOutputItem;
import io.github.sashirestela.openai.domain.response.Input.Item.FileSearchCallItem;
import io.github.sashirestela.openai.domain.response.Input.Item.FunctionCallItem;
import io.github.sashirestela.openai.domain.response.Input.Item.FunctionCallOutputItem;
import io.github.sashirestela.openai.domain.response.Input.Item.InputMessageItem;
import io.github.sashirestela.openai.domain.response.Input.Item.OutputMessageItem;
import io.github.sashirestela.openai.domain.response.Input.Item.ReasoningItem;
import io.github.sashirestela.openai.domain.response.Input.Item.WebSearchCallItem;
import io.github.sashirestela.openai.domain.response.Input.ItemReference;
import io.github.sashirestela.openai.domain.response.Input.ItemStatus;
import io.github.sashirestela.openai.domain.response.Input.MessageRole;
import io.github.sashirestela.openai.domain.response.Input.OutputContent.RefusalOutputContent;
import io.github.sashirestela.openai.domain.response.Input.OutputContent.TextOutputContent;
import io.github.sashirestela.openai.domain.response.Input.ReasoningContent;
import io.github.sashirestela.openai.domain.response.Input.SafetyCheck;
import io.github.sashirestela.openai.domain.response.Input.ScreenshotImage;
import io.github.sashirestela.openai.domain.response.Input.SearchStatus;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InputTest {

    @Test
    void testSerDeserInputClasses() {
        var oneInputMessage = InputMessage.of("message", MessageRole.DEVELOPER);
        var newInputMessage = JsonUtil.jsonToObject(JsonUtil.objectToJson(oneInputMessage),
                InputMessage.class);
        assertEquals(oneInputMessage.toString(), newInputMessage.toString());

        var oneItemReference = ItemReference.of("id");
        var newItemReference = JsonUtil.jsonToObject(JsonUtil.objectToJson(oneItemReference),
                ItemReference.class);
        assertEquals(oneItemReference.toString(), newItemReference.toString());
    }

    @Test
    void testSerDeserContentClasses() {
        var oneTextInputContent = TextInputContent.of("text");
        var newTextInputContent = JsonUtil.jsonToObject(JsonUtil.objectToJson(oneTextInputContent),
                TextInputContent.class);
        assertEquals(oneTextInputContent.toString(), newTextInputContent.toString());

        var oneImageInputContent = ImageInputContent.of("imageUrl");
        var newImageInputContent = JsonUtil.jsonToObject(JsonUtil.objectToJson(oneImageInputContent),
                ImageInputContent.class);
        assertEquals(oneImageInputContent.toString(), newImageInputContent.toString());

        var oneFileInputContent = FileInputContent.builder()
                .fileId("fileId")
                .fileData("fileData")
                .filename("filename")
                .build();
        var newFileInputContent = JsonUtil.jsonToObject(JsonUtil.objectToJson(oneFileInputContent),
                FileInputContent.class);
        assertEquals(oneFileInputContent.toString(), newFileInputContent.toString());
    }

    @Test
    void testSerDeserInputItemClasses() {
        var oneInputMessageItem = InputMessageItem.builder()
                .content(List.of(TextInputContent.of("text")))
                .role(MessageRole.DEVELOPER)
                .status(ItemStatus.COMPLETED)
                .build();
        var newInputMessageItem = JsonUtil.jsonToObject(JsonUtil.objectToJson(oneInputMessageItem),
                InputMessageItem.class);
        assertEquals(oneInputMessageItem.toString(), newInputMessageItem.toString());

        var oneOutputMessageItem = OutputMessageItem.builder()
                .content(List.of(TextOutputContent.of(List.of(FilePath.of("fileId", 0)), "text")))
                .id("id")
                .status(ItemStatus.IN_PROGRESS)
                .build();
        var newOutputMessageItem = JsonUtil.jsonToObject(JsonUtil.objectToJson(oneOutputMessageItem),
                OutputMessageItem.class);
        assertEquals(oneOutputMessageItem.toString(), newOutputMessageItem.toString());

        var oneFileSearchCallItem = FileSearchCallItem.builder()
                .id("id")
                .queries(List.of("query1", "query2"))
                .status(SearchStatus.COMPLETED)
                .results(List.of(FileSearchResult.builder()
                        .attributes(Map.of("key1", 10, "key2", "value"))
                        .fileId("fileId")
                        .filename("filename")
                        .score(0.8)
                        .text("text")
                        .build()))
                .build();
        var newFileSearchCallItem = JsonUtil.jsonToObject(JsonUtil.objectToJson(oneFileSearchCallItem),
                FileSearchCallItem.class);
        assertEquals(oneFileSearchCallItem.toString(), newFileSearchCallItem.toString());

        var oneComputerCallItem = ComputerCallItem.builder()
                .action(ScreenshotAction.of())
                .callId("calId")
                .id("id")
                .pendingSafetyChecks(List.of(SafetyCheck.builder()
                        .id("id")
                        .code("code")
                        .message("message")
                        .build()))
                .status(ItemStatus.INCOMPLETE)
                .build();
        var newComputerCallItem = JsonUtil.jsonToObject(JsonUtil.objectToJson(oneComputerCallItem),
                ComputerCallItem.class);
        assertEquals(oneComputerCallItem.toString(), newComputerCallItem.toString());

        var oneComputerCallOutputItem = ComputerCallOutputItem.builder()
                .callId("callId")
                .output(ScreenshotImage.builder()
                        .fileId("fileId")
                        .imageUrl("imageUrl")
                        .build())
                .acknowledgedSafetyChecks(List.of(SafetyCheck.builder()
                        .id("id")
                        .code("code")
                        .message("message")
                        .build()))
                .id("id")
                .status(ItemStatus.COMPLETED)
                .build();
        var newComputerCallOutputItem = JsonUtil.jsonToObject(JsonUtil.objectToJson(oneComputerCallOutputItem),
                ComputerCallOutputItem.class);
        assertEquals(oneComputerCallOutputItem.toString(), newComputerCallOutputItem.toString());

        var oneWebSearchCallItem = WebSearchCallItem.builder()
                .id("id")
                .status(SearchStatus.SEARCHING)
                .build();
        var newWebSearchCallItem = JsonUtil.jsonToObject(JsonUtil.objectToJson(oneWebSearchCallItem),
                WebSearchCallItem.class);
        assertEquals(oneWebSearchCallItem.toString(), newWebSearchCallItem.toString());

        var oneFunctionCallItem = FunctionCallItem.builder()
                .arguments("arguments")
                .callId("callId")
                .name("name")
                .id("id")
                .status(ItemStatus.IN_PROGRESS)
                .build();
        var newFunctionCallItem = JsonUtil.jsonToObject(JsonUtil.objectToJson(oneFunctionCallItem),
                FunctionCallItem.class);
        assertEquals(oneFunctionCallItem.toString(), newFunctionCallItem.toString());

        var oneFunctionCallOutputItem = FunctionCallOutputItem.builder()
                .callId("callId")
                .output("output")
                .id("id")
                .status(ItemStatus.COMPLETED)
                .build();
        var newFunctionCallOutputItem = JsonUtil.jsonToObject(JsonUtil.objectToJson(oneFunctionCallOutputItem),
                FunctionCallOutputItem.class);
        assertEquals(oneFunctionCallOutputItem.toString(), newFunctionCallOutputItem.toString());

        var oneReasoningItem = ReasoningItem.builder()
                .id("id")
                .summary(List.of(ReasoningContent.of("text")))
                .encryptedContent("encryptedContent")
                .status(ItemStatus.COMPLETED)
                .build();
        var newReasoningItem = JsonUtil.jsonToObject(JsonUtil.objectToJson(oneReasoningItem),
                ReasoningItem.class);
        assertEquals(oneReasoningItem.toString(), newReasoningItem.toString());
    }

    @Test
    void testSerDeserOutputContentClasses() {
        var oneTextOutputContent = TextOutputContent.of(List.of(FilePath.of("fileId", 0)), "text");
        var newTextOutputContent = JsonUtil.jsonToObject(JsonUtil.objectToJson(oneTextOutputContent),
                TextOutputContent.class);
        assertEquals(oneTextOutputContent.toString(), newTextOutputContent.toString());

        var oneRefusalOutputContent = RefusalOutputContent.of("refusal");
        var newRefusalOutputContent = JsonUtil.jsonToObject(JsonUtil.objectToJson(oneRefusalOutputContent),
                RefusalOutputContent.class);
        assertEquals(oneRefusalOutputContent.toString(), newRefusalOutputContent.toString());
    }

    @Test
    void testSerDeserCitationClasses() {
        var oneFileCitation = FileCitation.builder()
                .fileId("fileId")
                .index(2)
                .filename("filename")
                .build();
        var newFileCitation = JsonUtil.jsonToObject(JsonUtil.objectToJson(oneFileCitation),
                FileCitation.class);
        assertEquals(oneFileCitation.toString(), newFileCitation.toString());

        var oneUrlCitation = UrlCitation.builder()
                .endIndex(1000)
                .startIndex(500)
                .title("title")
                .url("url")
                .build();
        var newUrlCitation = JsonUtil.jsonToObject(JsonUtil.objectToJson(oneUrlCitation),
                UrlCitation.class);
        assertEquals(oneUrlCitation.toString(), newUrlCitation.toString());

        var oneFilePath = FilePath.of("fileId", 3);
        var newFilePath = JsonUtil.jsonToObject(JsonUtil.objectToJson(oneFilePath),
                FilePath.class);
        assertEquals(oneFilePath.toString(), newFilePath.toString());
    }

}
