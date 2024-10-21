package io.github.sashirestela.openai.common.content;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.github.sashirestela.openai.common.audio.InputAudioFormat;
import io.github.sashirestela.slimvalidator.constraints.Required;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ContentPart.ContentPartImageFile.class, name = "image_file"),
        @JsonSubTypes.Type(value = ContentPart.ContentPartImageUrl.class, name = "image_url"),
        @JsonSubTypes.Type(value = ContentPart.ContentPartInputAudio.class, name = "input_audio"),
        @JsonSubTypes.Type(value = ContentPart.ContentPartRefusal.class, name = "refusal"),
        @JsonSubTypes.Type(value = ContentPart.ContentPartTextAnnotation.class, name = "text")
})
@NoArgsConstructor
@Getter
@ToString
public abstract class ContentPart {

    protected ContentPartType type;

    public enum ContentPartType {

        @JsonProperty("text")
        TEXT,

        @JsonProperty("image_url")
        IMAGE_URL,

        @JsonProperty("image_file")
        IMAGE_FILE,

        @JsonProperty("input_audio")
        INPUT_AUDIO,

        @JsonProperty("refusal")
        REFUSAL;

    }

    public abstract static class ChatContentPart extends ContentPart {
    }

    @NoArgsConstructor
    @Getter
    @ToString
    public static class ContentPartText extends ChatContentPart {

        @Required
        private Object text;

        private ContentPartText(String text) {
            this.type = ContentPartType.TEXT;
            this.text = text;
        }

        public static ContentPartText of(String text) {
            return new ContentPartText(text);
        }

    }

    @NoArgsConstructor
    @Getter
    @ToString
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ContentPartImageUrl extends ChatContentPart {

        @Required
        private ImageUrl imageUrl;

        private ContentPartImageUrl(ImageUrl imageUrl) {
            this.type = ContentPartType.IMAGE_URL;
            this.imageUrl = imageUrl;
        }

        public static ContentPartImageUrl of(ImageUrl imageUrl) {
            return new ContentPartImageUrl(imageUrl);
        }

        @NoArgsConstructor
        @Getter
        @ToString
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

        }

    }

    @NoArgsConstructor
    @Getter
    @ToString
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ContentPartInputAudio extends ChatContentPart {

        @Required
        private InputAudio inputAudio;

        private ContentPartInputAudio(InputAudio inputAudio) {
            this.type = ContentPartType.INPUT_AUDIO;
            this.inputAudio = inputAudio;
        }

        public static ContentPartInputAudio of(InputAudio inputAudio) {
            return new ContentPartInputAudio(inputAudio);
        }

        @NoArgsConstructor
        @Getter
        @ToString
        @JsonInclude(Include.NON_EMPTY)
        public static class InputAudio {

            @Required
            private String data;

            @Required
            private InputAudioFormat format;

            private InputAudio(String data, InputAudioFormat format) {
                this.data = data;
                this.format = format;
            }

            public static InputAudio of(String data, InputAudioFormat format) {
                return new InputAudio(data, format);
            }

        }

    }

    @NoArgsConstructor
    @Getter
    @ToString
    public static class ContentPartRefusal extends ChatContentPart {

        @Required
        private String refusal;

        private ContentPartRefusal(String refusal) {
            this.type = ContentPartType.REFUSAL;
            this.refusal = refusal;
        }

        public static ContentPartRefusal of(String refusal) {
            return new ContentPartRefusal(refusal);
        }

    }

    @NoArgsConstructor
    @Getter
    @ToString
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

        @NoArgsConstructor
        @Getter
        @ToString
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

    @NoArgsConstructor
    @Getter
    @ToString
    public static class ContentPartTextAnnotation extends ContentPartText {

        private TextAnnotation text;

        @NoArgsConstructor
        @Getter
        @ToString
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class TextAnnotation {

            private String value;
            private List<FileAnnotation> annotations;

        }

    }

}
