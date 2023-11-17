package io.github.sashirestela.openai.domain.chat;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.github.sashirestela.openai.domain.chat.message.ChatMsgResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
public class Choice {

    private Integer index;

    @JsonAlias({ "delta" })
    private ChatMsgResponse message;

    @JsonProperty("finish_reason")
    private String finishReason;

}