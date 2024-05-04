package io.github.sashirestela.openai.domain.image;

import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.domain.DomainTestingHelper;
import io.github.sashirestela.openai.domain.image.ImageRequest.Quality;
import io.github.sashirestela.openai.domain.image.ImageRequest.Style;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.http.HttpClient;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class ImageDomainTest {

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
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/images_create.json");
        var imageRequest = ImageRequest.builder()
                .prompt("A cartoon of a hummingbird that is flying around a flower.")
                .n(2)
                .size(Size.X1024)
                .responseFormat(ImageRespFmt.URL)
                .model("dall-e-3")
                .quality(Quality.STANDARD)
                .style(Style.NATURAL)
                .user("test")
                .build();
        var imageResponse = openAI.images().create(imageRequest).join();
        System.out.println(imageResponse);
        assertNotNull(imageResponse);
    }

    @Test
    void testFilesCreateEditions() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/images_create_edits.json");
        var imageRequest = ImageEditsRequest.builder()
                .image(Paths.get("src/demo/resources/little_cat_rgba.png"))
                .prompt("A cartoon of a little cute cat playing with a ball in the grass.")
                .n(1)
                .size(Size.X256)
                .responseFormat(ImageRespFmt.B64JSON)
                .model("dall-e-2")
                .user("test")
                .build();
        var imageResponse = openAI.images().createEdits(imageRequest).join();
        System.out.println(imageResponse);
        assertNotNull(imageResponse);
    }

    @Test
    void testFilesCreateVariations() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/images_create_variations.json");
        var imageRequest = ImageVariationsRequest.builder()
                .image(Paths.get("src/demo/resources/little_cat.png"))
                .n(1)
                .size(Size.X256)
                .responseFormat(ImageRespFmt.B64JSON)
                .model("dall-e-2")
                .user("test")
                .build();
        var imageResponse = openAI.images().createVariations(imageRequest).join();
        System.out.println(imageResponse);
        assertNotNull(imageResponse);
    }

}
