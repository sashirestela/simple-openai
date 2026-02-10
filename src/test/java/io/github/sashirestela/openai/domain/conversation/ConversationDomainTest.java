package io.github.sashirestela.openai.domain.conversation;

import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.domain.DomainTestingHelper;
import io.github.sashirestela.openai.domain.response.Input.InputMessage;
import io.github.sashirestela.openai.domain.response.Input.MessageRole;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.http.HttpClient;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class ConversationDomainTest {

    static HttpClient httpClient;
    static SimpleOpenAI openAI;

    @BeforeAll
    static void setup() {
        httpClient = mock(HttpClient.class);
        openAI = SimpleOpenAI.builder()
                .apiKey("apiKey")
                .httpClient(httpClient)
                .build();
    }

    @Test
    void testCreateConversation() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/conversations_create.json");
        var conversationRequest = ConversationRequest.builder()
                .metadata(Map.of("test", "value"))
                .build();
        var conversation = openAI.conversations().create(conversationRequest).join();
        System.out.println(conversation);
        assertNotNull(conversation);
    }

    @Test
    void testCreateMinimalConversation() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/conversations_create.json");
        var conversation = openAI.conversations().create().join();
        System.out.println(conversation);
        assertNotNull(conversation);
    }

    @Test
    void testRetrieveConversation() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/conversations_getone.json");
        var conversation = openAI.conversations().getOne("conversationId").join();
        System.out.println(conversation);
        assertNotNull(conversation);
    }

    @Test
    void testModifyConversation() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/conversations_modify.json");
        var modifyRequest = ConversationModifyRequest.builder()
                .metadata(Map.of("test", "updated"))
                .build();
        var conversation = openAI.conversations().modify("conversationId", modifyRequest).join();
        System.out.println(conversation);
        assertNotNull(conversation);
    }

    @Test
    void testDeleteConversation() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/conversations_delete.json");
        var deletedConversation = openAI.conversations().delete("conversationId").join();
        System.out.println(deletedConversation);
        assertNotNull(deletedConversation);
    }

    @Test
    void testCreateConversationItems() throws IOException {
        DomainTestingHelper.get()
                .mockForObject(httpClient,
                        "src/test/resources/conversations_items_create.json");
        var itemsRequest = ConversationItemsRequest.builder()
                .item(InputMessage.of("Hello, how are you?", MessageRole.USER))
                .build();
        var items = openAI.conversationItems().create("conversationId", itemsRequest).join();
        System.out.println(items);
        assertNotNull(items);
    }

    @Test
    void testListConversationItems() throws IOException {
        DomainTestingHelper.get()
                .mockForObject(httpClient,
                        "src/test/resources/conversations_items_getlist.json");
        var items = openAI.conversationItems().getList("conversationId").join();
        System.out.println(items);
        assertNotNull(items);
    }

    @Test
    void testGetConversationItem() throws IOException {
        DomainTestingHelper.get()
                .mockForObject(httpClient,
                        "src/test/resources/conversations_items_getone.json");
        var item = openAI.conversationItems().getOne("conversationId", "itemId").join();
        System.out.println(item);
        assertNotNull(item);
        assertNotNull(item.getItem());
    }

    @Test
    void testDeleteConversationItem() throws IOException {
        DomainTestingHelper.get()
                .mockForObject(httpClient,
                        "src/test/resources/conversations_getone.json");
        var conversation = openAI.conversationItems().delete("conversationId", "itemId").join();
        System.out.println(conversation);
        assertNotNull(conversation);
    }

}
