package io.github.sashirestela.openai.domain.image;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@NoArgsConstructor
@Getter
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Image {

    private Integer created;
    private List<ImageData> data;
    private ImageUsage usage;

    @NoArgsConstructor
    @Getter
    @ToString
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ImageData {

        private String url;
        private String b64Json;
        private String revisedPrompt;

    }

    @NoArgsConstructor
    @Getter
    @ToString
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ImageUsage {

        private Integer inputTokens;
        private Integer outputTokens;
        private Integer totalTokens;
        private ImageTokensDetails inputTokenDetails;

    }

    @NoArgsConstructor
    @Getter
    @ToString
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ImageTokensDetails {

        private Integer imageTokens;
        private Integer textTokens;

    }

}
