package io.github.sashirestela.openai.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.github.sashirestela.openai.common.ContentPart.ContentPartImage.ImageUrl.ImageDetail;
import io.github.sashirestela.slimvalidator.constraints.Required;
import lombok.Getter;

@Getter
public abstract class ContentPart {

    protected ContentPartType type;

    public enum ContentPartType {

        @JsonProperty("text")
        TEXT,

        @JsonProperty("image_url")
        IMAGE_URL,

        @JsonProperty("image_file")
        IMAGE_FILE;

    }

    public abstract static class ChatContentPart extends ContentPart {
    }

    @Getter
    public static class ContentPartText extends ChatContentPart {

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
    public static class ContentPartImage extends ChatContentPart {

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

    @Getter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ContentPartImageFile extends ContentPart {

        @Required
        private ImageFile imageFile;

        private ContentPartImageFile(ImageFile imageFile) {
            this.type = ContentPartType.IMAGE_FILE;
            this.imageFile = imageFile;
        }

        public static ContentPartImageFile of(ImageFile imageFile) {
            return new ContentPartImageFile(imageFile);
        }

        @Getter
        @JsonInclude(Include.NON_EMPTY)
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class ImageFile {

            @Required
            private String fileId;

            private ImageDetail detail;

            private ImageFile(String fileId, ImageDetail detail) {
                this.fileId = fileId;
                this.detail = detail;
            }

            public static ImageFile of(String fileId, ImageDetail detail) {
                return new ImageFile(fileId, detail);
            }

            public static ImageFile of(String fileId) {
                return new ImageFile(fileId, null);
            }

        }

    }

}
