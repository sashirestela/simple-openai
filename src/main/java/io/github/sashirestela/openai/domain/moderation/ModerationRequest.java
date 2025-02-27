package io.github.sashirestela.openai.domain.moderation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.github.sashirestela.slimvalidator.constraints.ObjectType;
import io.github.sashirestela.slimvalidator.constraints.Required;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Builder
@JsonInclude(Include.NON_EMPTY)
public class ModerationRequest {

    @Required
    @ObjectType(baseClass = String.class)
    @ObjectType(baseClass = String.class, firstGroup = true)
    @ObjectType(baseClass = MultiModalInput.class, firstGroup = true)
    private Object input;

    private String model;

    public enum MultiModalType {

        @JsonProperty("text")
        TEXT,

        @JsonProperty("image_url")
        IMAGE_URL;

    }

    @Getter
    @Setter
    public abstract static class MultiModalInput {

        protected MultiModalType type;

        @Getter
        @ToString
        @JsonInclude(Include.NON_EMPTY)
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class TextInput extends MultiModalInput {

            @Required
            private String text;

            private TextInput(String text) {
                this.type = MultiModalType.TEXT;
                this.text = text;
            }

            public static TextInput of(String text) {
                return new TextInput(text);
            }

        }

        @Getter
        @ToString
        @JsonInclude(Include.NON_EMPTY)
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class ImageUrlInput extends MultiModalInput {

            @Required
            private ImageUrl imageUrl;

            private ImageUrlInput(ImageUrl imageUrl) {
                this.type = MultiModalType.IMAGE_URL;
                this.imageUrl = imageUrl;
            }

            public static ImageUrlInput of(String url) {
                return new ImageUrlInput(new ImageUrl(url));
            }

            @Getter
            @ToString
            public static class ImageUrl {

                @Required
                private String url;

                public ImageUrl(String url) {
                    this.url = url;
                }

            }

        }

    }

}
