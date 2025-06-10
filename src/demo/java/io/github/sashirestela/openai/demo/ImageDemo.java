package io.github.sashirestela.openai.demo;

import io.github.sashirestela.openai.domain.image.Background;
import io.github.sashirestela.openai.domain.image.ImageEditsRequest;
import io.github.sashirestela.openai.domain.image.ImageRequest;
import io.github.sashirestela.openai.domain.image.ImageRequest.Moderation;
import io.github.sashirestela.openai.domain.image.ImageRequest.OutputFormat;
import io.github.sashirestela.openai.domain.image.ImageResponseFormat;
import io.github.sashirestela.openai.domain.image.ImageVariationsRequest;
import io.github.sashirestela.openai.domain.image.Quality;
import io.github.sashirestela.openai.domain.image.Size;
import io.github.sashirestela.openai.support.Base64Util;

import java.nio.file.Paths;
import java.util.stream.IntStream;

public class ImageDemo extends AbstractDemo {

    private static final String MODEL = "dall-e-2";

    public void demoCallImageGeneration() {
        var imageRequest = ImageRequest.builder()
                .prompt("A cartoon of a hummingbird that is flying around a flower.")
                .n(2)
                .size(Size.X_256_256)
                .responseFormat(ImageResponseFormat.URL)
                .model(MODEL)
                .build();
        var futureImage = openAI.images().create(imageRequest);
        var imageResponse = futureImage.join();
        imageResponse.getData().stream().forEach(img -> System.out.println("\n" + img.getUrl()));
    }

    public void demoCallImageGeneration2() {
        var imageRequest = ImageRequest.builder()
                .prompt("An image of orange cat hugging other white cat with a light blue scarf.")
                .model("gpt-image-1")
                .background(Background.TRANSPARENT)
                .outputFormat(OutputFormat.PNG)
                .quality(Quality.MEDIUM)
                .size(Size.X_1024_1024)
                .moderation(Moderation.LOW)
                .n(2)
                .build();
        var futureImage = openAI.images().create(imageRequest);
        var imageResponse = futureImage.join();
        IntStream.range(0, imageResponse.getData().size()).forEach(i -> {
            var filePath = "src/demo/resources/image" + (i + 1) + ".png";
            Base64Util.decode(imageResponse.getData().get(i).getB64Json(), filePath);
            System.out.println(filePath);
        });
    }

    public void demoCallImageEdits() {
        var imageEditsRequest = ImageEditsRequest.builder()
                .image(Paths.get("src/demo/resources/little_cat_rgba.png"))
                .prompt("A cartoon of a little cute cat playing with a ball in the grass.")
                .n(1)
                .size(Size.X_256_256)
                .responseFormat(ImageResponseFormat.URL)
                .model(MODEL)
                .build();
        var futureImage = openAI.images().createEdits(imageEditsRequest);
        var imageResponse = futureImage.join();
        System.out.println(imageResponse.getData().get(0).getUrl());
    }

    public void demoCallImageVariations() {
        var imageVariationsRequest = ImageVariationsRequest.builder()
                .image(Paths.get("src/demo/resources/little_cat.png"))
                .n(1)
                .size(Size.X_256_256)
                .responseFormat(ImageResponseFormat.URL)
                .model(MODEL)
                .build();
        var futureImage = openAI.images().createVariations(imageVariationsRequest);
        var imageResponse = futureImage.join();
        System.out.println(imageResponse.getData().get(0).getUrl());
    }

    public static void main(String[] args) {
        var demo = new ImageDemo();

        demo.addTitleAction("Call Image Generation", demo::demoCallImageGeneration);
        demo.addTitleAction("Call Image Generation 2", demo::demoCallImageGeneration2);
        demo.addTitleAction("Call Image Edits", demo::demoCallImageEdits);
        demo.addTitleAction("Call Image Variations", demo::demoCallImageVariations);

        demo.run();
    }

}
