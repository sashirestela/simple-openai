package io.github.sashirestela.openai.domain.upload;

import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.domain.DomainTestingHelper;
import io.github.sashirestela.openai.domain.file.FileRequest.PurposeType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.http.HttpClient;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class UploadDomainTest {

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
    void testUploadsCreate() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/uploads_create.json");
        var uploadRequest = UploadRequest.builder()
                .filename("constitucion_politica_peru_2024.pdf")
                .purpose(PurposeType.ASSISTANTS)
                .bytes(4658052L)
                .mimeType("application/pdf")
                .build();
        var uploadResponse = openAI.uploads().create(uploadRequest).join();
        System.out.println(uploadResponse);
        assertNotNull(uploadResponse);
    }

    @Test
    void testUploadsAddPart() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/uploads_add_part.json");
        var uploadPartResponse = openAI.uploads()
                .addPart("upload_evo3LXvUbhjjwTzluS7HmLVK",
                        UploadPartRequest.builder()
                                .data(Path.of("src/demo/resources/mistral-ai.txt"))
                                .build())
                .join();
        System.out.println(uploadPartResponse);
        assertNotNull(uploadPartResponse);
    }

    @Test
    void testUploadsComplete() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/uploads_complete.json");
        var uploadCompleteResponse = openAI.uploads()
                .complete("upload_evo3LXvUbhjjwTzluS7HmLVK",
                        UploadCompleteRequest.builder()
                                .partIds(List.of(
                                        "part_6Jtu0dgFKKJcqZD7W8AjYNBp",
                                        "part_xUtazRLFXT3yiA1BuWxHBx6o",
                                        "part_ks9bMvxBRWC1mH7QLYIUFNmT",
                                        "part_DhPXxw0LHuii98e7FmSryu43",
                                        "part_omCqOsNVmLEL0OHRbDw1apuZ"))
                                .build())
                .join();
        System.out.println(uploadCompleteResponse);
    }

    @Test
    void testUploadsCancel() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/uploads_cancel.json");
        var uploadCancelResponse = openAI.uploads().cancel("upload_DSulbCDVm0KmatTxopmPWJhB").join();
        System.out.println(uploadCancelResponse);
    }

}
