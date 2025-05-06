package io.github.sashirestela.openai.domain.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sashirestela.openai.common.content.ImageDetail;
import io.github.sashirestela.openai.domain.response.Input.Content;
import io.github.sashirestela.openai.domain.response.Input.Content.ContentType;
import io.github.sashirestela.openai.domain.response.Input.Content.FileInputContent;
import io.github.sashirestela.openai.domain.response.Input.Content.ImageInputContent;
import io.github.sashirestela.openai.domain.response.Input.Content.TextInputContent;
import io.github.sashirestela.openai.domain.response.Input.InputMessage;
import io.github.sashirestela.openai.domain.response.Input.InputType;
import io.github.sashirestela.openai.domain.response.Input.Item.InputMessageItem;
import io.github.sashirestela.openai.domain.response.Input.Item.ItemStatus;
import io.github.sashirestela.openai.domain.response.Input.Item.OutputMessageItem;
import io.github.sashirestela.openai.domain.response.Input.ItemReference;
import io.github.sashirestela.openai.domain.response.Input.ItemType;
import io.github.sashirestela.openai.domain.response.Input.MessageRole;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InputTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void testInputMessageCreation() {
        // Test with string content
        String textContent = "This is a test message";
        InputMessage textMessage = InputMessage.of(textContent, MessageRole.USER);

        assertNotNull(textMessage);
        assertEquals(textContent, textMessage.getContent());
        assertEquals(MessageRole.USER, textMessage.getRole());
        assertEquals(InputType.MESSAGE, textMessage.getType());

        // Test with Content object
        TextInputContent contentObject = TextInputContent.of("This is a content object");
        InputMessage contentMessage = InputMessage.of(contentObject, MessageRole.SYSTEM);

        assertNotNull(contentMessage);
        assertEquals(contentObject, contentMessage.getContent());
        assertEquals(MessageRole.SYSTEM, contentMessage.getRole());
        assertEquals(InputType.MESSAGE, contentMessage.getType());
    }

    @Test
    void testInputMessageSerialization() throws JsonProcessingException {
        // Create test message
        InputMessage message = InputMessage.of("Test serialization", MessageRole.USER);

        // Serialize to JSON
        String json = mapper.writeValueAsString(message);

        // Deserialize back to InputMessage
        InputMessage deserialized = mapper.readValue(json, InputMessage.class);

        // Verify
        assertNotNull(deserialized);
        assertEquals("Test serialization", deserialized.getContent());
        assertEquals(MessageRole.USER, deserialized.getRole());
        assertEquals(InputType.MESSAGE, deserialized.getType());
    }

    @Test
    void testItemReferenceCreation() {
        String id = "item_12345";
        ItemReference itemRef = ItemReference.of(id);

        assertNotNull(itemRef);
        assertEquals(id, itemRef.getId());
        assertEquals(InputType.ITEM_REFERENCE, itemRef.getType());
    }

    @Test
    void testItemReferenceSerialization() throws JsonProcessingException {
        // Create test item reference
        ItemReference itemRef = ItemReference.of("item_67890");

        // Serialize to JSON
        String json = mapper.writeValueAsString(itemRef);

        // Deserialize back to ItemReference
        ItemReference deserialized = mapper.readValue(json, ItemReference.class);

        // Verify
        assertNotNull(deserialized);
        assertEquals("item_67890", deserialized.getId());
        assertEquals(InputType.ITEM_REFERENCE, deserialized.getType());
    }

    @Test
    void testTextInputContentCreation() {
        String text = "This is a text content";
        TextInputContent textContent = TextInputContent.of(text);

        assertNotNull(textContent);
        assertEquals(text, textContent.getText());
        assertEquals(ContentType.INPUT_TEXT, textContent.getType());
    }

    @Test
    void testTextInputContentSerialization() throws JsonProcessingException {
        // Create test text content
        TextInputContent content = TextInputContent.of("Serialization test");

        // Serialize to JSON
        String json = mapper.writeValueAsString(content);

        // Deserialize back to TextInputContent
        TextInputContent deserialized = mapper.readValue(json, TextInputContent.class);

        // Verify
        assertNotNull(deserialized);
        assertEquals("Serialization test", deserialized.getText());
        assertEquals(ContentType.INPUT_TEXT, deserialized.getType());
    }

    @Test
    void testImageInputContentCreation() {
        // Test with minimal parameters
        ImageInputContent minContent = ImageInputContent.of(ImageDetail.LOW);

        assertNotNull(minContent);
        assertEquals(ImageDetail.LOW, minContent.getDetail());
        assertEquals(ContentType.INPUT_IMAGE, minContent.getType());
        assertNull(minContent.getFileId());
        assertNull(minContent.getImageUrl());

        // Test with builder and all parameters
        ImageInputContent fullContent = ImageInputContent.builder()
                .detail(ImageDetail.HIGH)
                .fileId("file_abc123")
                .imageUrl("https://example.com/image.jpg")
                .build();

        assertNotNull(fullContent);
        assertEquals(ImageDetail.HIGH, fullContent.getDetail());
        assertEquals(ContentType.INPUT_IMAGE, fullContent.getType());
        assertEquals("file_abc123", fullContent.getFileId());
        assertEquals("https://example.com/image.jpg", fullContent.getImageUrl());
    }

    @Test
    void testImageInputContentSerialization() throws JsonProcessingException {
        // Create test image content
        ImageInputContent content = ImageInputContent.builder()
                .detail(ImageDetail.AUTO)
                .fileId("file_xyz789")
                .imageUrl("https://example.com/test.png")
                .build();

        // Serialize to JSON
        String json = mapper.writeValueAsString(content);

        // Deserialize back to ImageInputContent
        ImageInputContent deserialized = mapper.readValue(json, ImageInputContent.class);

        // Verify
        assertNotNull(deserialized);
        assertEquals(ImageDetail.AUTO, deserialized.getDetail());
        assertEquals(ContentType.INPUT_IMAGE, deserialized.getType());
        assertEquals("file_xyz789", deserialized.getFileId());
        assertEquals("https://example.com/test.png", deserialized.getImageUrl());
    }

    @Test
    void testFileInputContentCreation() {
        // Create test file content with all parameters
        FileInputContent content = FileInputContent.builder()
                .fileData("base64-encoded-data")
                .fileId("file_def456")
                .filename("document.pdf")
                .build();

        assertNotNull(content);
        assertEquals("base64-encoded-data", content.getFileData());
        assertEquals("file_def456", content.getFileId());
        assertEquals("document.pdf", content.getFilename());
        assertEquals(ContentType.INPUT_FILE, content.getType());
    }

    @Test
    void testFileInputContentSerialization() throws JsonProcessingException {
        // Create test file content
        FileInputContent content = FileInputContent.builder()
                .fileData("test-file-data")
                .fileId("file_123")
                .filename("test.txt")
                .build();

        // Serialize to JSON
        String json = mapper.writeValueAsString(content);

        // Deserialize back to FileInputContent
        FileInputContent deserialized = mapper.readValue(json, FileInputContent.class);

        // Verify
        assertNotNull(deserialized);
        assertEquals("test-file-data", deserialized.getFileData());
        assertEquals("file_123", deserialized.getFileId());
        assertEquals("test.txt", deserialized.getFilename());
        assertEquals(ContentType.INPUT_FILE, deserialized.getType());
    }

    @Test
    void testInputMessageItemCreation() {
        // Create test contents
        List<Content> contents = Arrays.asList(
                TextInputContent.of("Text content"),
                ImageInputContent.of(ImageDetail.LOW));

        // Create input message item
        InputMessageItem item = InputMessageItem.builder()
                .content(contents)
                .role(MessageRole.USER)
                .status(ItemStatus.COMPLETED)
                .build();

        assertNotNull(item);
        assertEquals(contents, item.getContent());
        assertEquals(MessageRole.USER, item.getRole());
        assertEquals(ItemStatus.COMPLETED, item.getStatus());
        assertEquals(ItemType.MESSAGE, item.getType());
    }

    @Test
    void testInputMessageItemSerialization() throws JsonProcessingException {
        // Create test content
        List<Content> contents = Collections.singletonList(TextInputContent.of("Serialization test"));

        // Create input message item
        InputMessageItem item = InputMessageItem.builder()
                .content(contents)
                .role(MessageRole.DEVELOPER)
                .status(ItemStatus.IN_PROGRESS)
                .build();

        // Serialize to JSON
        String json = mapper.writeValueAsString(item);

        // Verify the serialized JSON directly instead of deserializing
        assertTrue(json.contains("\"content\":["));
        assertTrue(json.contains("\"text\":\"Serialization test\""));
        assertTrue(json.contains("\"type\":\"input_text\""));
        assertTrue(json.contains("\"role\":\"developer\""));
        assertTrue(json.contains("\"status\":\"in_progress\""));
        assertTrue(json.contains("\"type\":\"message\""));
    }

    @Test
    void testOutputMessageItemCreation() {
        // Create test content
        List<Object> content = Arrays.asList("Text response", 123);

        // Create output message item
        OutputMessageItem item = OutputMessageItem.builder()
                .content(content)
                .id("msg_abc123")
                .status(ItemStatus.COMPLETED)
                .build();

        assertNotNull(item);
        assertEquals(content, item.getContent());
        assertEquals("msg_abc123", item.getId());
        assertEquals(ItemStatus.COMPLETED, item.getStatus());
        assertEquals(MessageRole.ASSISTANT, item.getRole()); // Always ASSISTANT for output messages
        assertEquals(ItemType.MESSAGE, item.getType());
    }

    @Test
    void testOutputMessageItemSerialization() throws JsonProcessingException {
        // Create test content
        List<Object> content = Collections.singletonList("Response content");

        // Create output message item
        OutputMessageItem item = OutputMessageItem.builder()
                .content(content)
                .id("msg_xyz789")
                .status(ItemStatus.INCOMPLETE)
                .build();

        // Serialize to JSON
        String json = mapper.writeValueAsString(item);

        // Deserialize back to OutputMessageItem
        OutputMessageItem deserialized = mapper.readValue(json, OutputMessageItem.class);

        // Verify
        assertNotNull(deserialized);
        assertEquals(1, deserialized.getContent().size());
        assertEquals("Response content", deserialized.getContent().get(0));
        assertEquals("msg_xyz789", deserialized.getId());
        assertEquals(ItemStatus.INCOMPLETE, deserialized.getStatus());
        assertEquals(MessageRole.ASSISTANT, deserialized.getRole());
        assertEquals(ItemType.MESSAGE, deserialized.getType());
    }

    @Test
    void testEnumSerialization() throws JsonProcessingException {
        // Test MessageRole serialization
        assertEquals("\"user\"", mapper.writeValueAsString(MessageRole.USER));
        assertEquals("\"assistant\"", mapper.writeValueAsString(MessageRole.ASSISTANT));
        assertEquals("\"system\"", mapper.writeValueAsString(MessageRole.SYSTEM));
        assertEquals("\"developer\"", mapper.writeValueAsString(MessageRole.DEVELOPER));

        // Test InputType serialization
        assertEquals("\"message\"", mapper.writeValueAsString(InputType.MESSAGE));
        assertEquals("\"item_reference\"", mapper.writeValueAsString(InputType.ITEM_REFERENCE));

        // Test ItemStatus serialization
        assertEquals("\"in_progress\"", mapper.writeValueAsString(ItemStatus.IN_PROGRESS));
        assertEquals("\"completed\"", mapper.writeValueAsString(ItemStatus.COMPLETED));
        assertEquals("\"incomplete\"", mapper.writeValueAsString(ItemStatus.INCOMPLETE));

        // Test ContentType serialization
        assertEquals("\"input_text\"", mapper.writeValueAsString(ContentType.INPUT_TEXT));
        assertEquals("\"input_image\"", mapper.writeValueAsString(ContentType.INPUT_IMAGE));
        assertEquals("\"input_file\"", mapper.writeValueAsString(ContentType.INPUT_FILE));

        // Test ItemType serialization
        assertEquals("\"message\"", mapper.writeValueAsString(ItemType.MESSAGE));
        assertEquals("\"file_search_call\"", mapper.writeValueAsString(ItemType.FILE_SEARCH_CALL));
        assertEquals("\"computer_call\"", mapper.writeValueAsString(ItemType.COMPUTER_CALL));
        assertEquals("\"computer_call_output\"", mapper.writeValueAsString(ItemType.COMPUTER_CALL_OUTPUT));
        assertEquals("\"web_search_call\"", mapper.writeValueAsString(ItemType.WEB_SEARCH_CALL));
        assertEquals("\"function_call\"", mapper.writeValueAsString(ItemType.FUNCTION_CALL));
        assertEquals("\"function_call_output\"", mapper.writeValueAsString(ItemType.FUNCTION_CALL_OUTPUT));
        assertEquals("\"reasoning\"", mapper.writeValueAsString(ItemType.REASONING));
    }

    @Test
    void testComplexJsonSerialization() throws JsonProcessingException {
        // Create complex input message with content object
        TextInputContent textContent = TextInputContent.of("Complex test message");
        InputMessage message = InputMessage.of(textContent, MessageRole.USER);

        // Serialize to JSON
        String json = mapper.writeValueAsString(message);

        // Print JSON for debugging
        System.out.println("Serialized JSON: " + json);

        // Verify JSON structure (using more specific patterns)
        assertTrue(json.contains("\"content\":{"));
        assertTrue(json.contains("\"type\":\"input_text\""));
        assertTrue(json.contains("\"text\":\"Complex test message\""));
        assertTrue(json.contains("\"role\":\"user\""));

        // For now, we'll just verify these key parts of the structure
        // rather than attempting deserialization of complex objects
    }

}
