package io.github.sashirestela.openai.domain.chat.content;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.NonNull;

@Getter
@JsonInclude(Include.NON_EMPTY)
public class ImageUrl {

    private String url;
    private ImageDetail detail;

    public ImageUrl(@NonNull String url, ImageDetail detail) {
        this.url = url;
        this.detail = detail;
    }

    public ImageUrl(String url) {
        this(url, null);
    }

}
