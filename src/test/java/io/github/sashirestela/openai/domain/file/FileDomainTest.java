package io.github.sashirestela.openai.domain.file;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.net.http.HttpClient;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.domain.DomainTestingHelper;

class FileDomainTest {

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
    void testFilesCreate() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/files_create.json");
        var fileRequest = FileRequest.builder()
                .file(Paths.get("src/demo/resources/test_data.jsonl"))
                .purpose("fine-tune")
                .build();
        var fileResponse = openAI.files().create(fileRequest).join();
        System.out.println(fileResponse);
        assertNotNull(fileResponse);
    }

    @Test
    void testFilesGetList() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/files_getlist.json");
        var fileResponse = openAI.files().getList().join();
        System.out.println(fileResponse);
        assertNotNull(fileResponse);
    }

    @Test
    void testFilesGetOne() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/files_getone.json");
        var fileResponse = openAI.files().getOne("fileId").join();
        System.out.println(fileResponse);
        assertNotNull(fileResponse);
    }

    @Test
    void testFilesGetContent() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/files_getcontent.txt");
        var fileResponse = openAI.files().getContent("fileId").join();
        System.out.println(fileResponse);
        assertNotNull(fileResponse);
    }

    @Test
    void testFilesDelete() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/files_delete.json");
        var fileResponse = openAI.files().delete("fileId").join();
        System.out.println(fileResponse);
        assertNotNull(fileResponse);
    }
}