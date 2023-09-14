package io.github.sashirestela.openai.domain.image;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.net.http.HttpClient;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.domain.DomainTestingHelper;

public class ImageDomainTest {

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
        .size(Size.X256)
        .responseFormat(ImageRespFmt.URL)
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
        .user("test")
        .build();
    var imageResponse = openAI.images().createVariations(imageRequest).join();
    System.out.println(imageResponse);
    assertNotNull(imageResponse);
  }
}