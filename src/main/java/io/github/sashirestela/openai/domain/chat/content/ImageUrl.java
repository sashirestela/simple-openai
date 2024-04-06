package io.github.sashirestela.openai.domain.chat.content;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.github.sashirestela.slimvalidator.constraints.Required;
import lombok.Getter;

@Getter
@JsonInclude(Include.NON_EMPTY)
public class ImageUrl {

    @Required
    private String url;

    private ImageDetail detail;

    public ImageUrl(String url, ImageDetail detail) {
        this.url = url;
        this.detail = detail;
    }

    public ImageUrl(String url) {
        this(url, null);
    }

}
