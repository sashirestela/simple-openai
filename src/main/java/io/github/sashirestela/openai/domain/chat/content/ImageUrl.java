package io.github.sashirestela.openai.domain.chat.content;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.NonNull;

@Getter
public class ImageUrl {
    
    private String url;

    @JsonInclude(Include.NON_NULL)
    private ImageDetail detail;

	public ImageUrl(@NonNull String url, ImageDetail detail) {
		this.url = url;
		this.detail = detail;
	}

	public ImageUrl(String url) {
		this(url, null);
	}
}