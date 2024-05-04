package io.github.sashirestela.openai.domain.chat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.github.sashirestela.slimvalidator.constraints.Required;
import lombok.Getter;

@Getter
public abstract class ContentPart {

    protected ContentPartType type;

    public enum ContentPartType {

        @JsonProperty("text")
        TEXT,

        @JsonProperty("image_url")
        IMAGE_URL;

    }

    @Getter
    public static class ContentPartText extends ContentPart {

        @Required
        private String text;

        private ContentPartText(String text) {
            this.type = ContentPartType.TEXT;
            this.text = text;
        }

        public static ContentPartText of(String text) {
            return new ContentPartText(text);
        }

    }

    @Getter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ContentPartImage extends ContentPart {

        @Required
        private ImageUrl imageUrl;

        private ContentPartImage(ImageUrl imageUrl) {
            this.type = ContentPartType.IMAGE_URL;
            this.imageUrl = imageUrl;
        }

        public static ContentPartImage of(ImageUrl imageUrl) {
            return new ContentPartImage(imageUrl);
        }

        @Getter
        @JsonInclude(Include.NON_EMPTY)
        public static class ImageUrl {

            @Required
            private String url;

            private ImageDetail detail;

            private ImageUrl(String url, ImageDetail detail) {
                this.url = url;
                this.detail = detail;
            }

            public static ImageUrl of(String url, ImageDetail detail) {
                return new ImageUrl(url, detail);
            }

            public static ImageUrl of(String url) {
                return new ImageUrl(url, null);
            }

            public enum ImageDetail {

                @JsonProperty("auto")
                AUTO,

                @JsonProperty("low")
                LOW,

                @JsonProperty("high")
                HIGH;

            }

        }

    }

}
