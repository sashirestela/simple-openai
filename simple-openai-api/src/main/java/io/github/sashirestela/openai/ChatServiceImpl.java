package io.github.sashirestela.openai;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.stream.Stream;

import io.github.sashirestela.openai.chat.ChatService;
import io.github.sashirestela.openai.chat.ChatRequest;
import io.github.sashirestela.openai.chat.ChatResponse;
import io.github.sashirestela.openai.utils.Constant;
import io.github.sashirestela.openai.utils.JsonUtil;
import io.github.sashirestela.openai.utils.Url;

class ChatServiceImpl implements ChatService {
  private String apiKey;
  private HttpClient httpClient;

  public ChatServiceImpl(HttpClient httpClient, String apiKey) {
    this.httpClient = httpClient;
    this.apiKey = apiKey;
  }

	@Override
	public ChatResponse callChatCompletion(ChatRequest chatRequest)
    throws IOException, InterruptedException {
    chatRequest.setStream(false);
    String requestJson = JsonUtil.one().objectToJson(chatRequest);
    HttpRequest httpRequest = HttpRequest.newBuilder()
      .uri(URI.create(Url.OPENAI_CHAT_COMPLETIONS.value))
      .header(Constant.HEADER_CONTENT_TYPE, Constant.CONTENT_JSON)
      .header(Constant.HEADER_AUTHORIZATION, Constant.AUTH_BEARER + apiKey)
      .POST(BodyPublishers.ofString(requestJson))
      .build();
    HttpResponse<String> httpResponse = httpClient.send(
      httpRequest, BodyHandlers.ofString()
    );
    ChatResponse chatResponse = JsonUtil.one()
      .jsonToObject(httpResponse.body(), ChatResponse.class);
		return chatResponse;
	}

	@Override
	public Stream<ChatResponse> callChatCompletionStream(ChatRequest chatRequest)
    throws IOException, InterruptedException {
    chatRequest.setStream(true);
    String requestJson = JsonUtil.one().objectToJson(chatRequest);
    HttpRequest httpRequest = HttpRequest.newBuilder()
      .uri(URI.create(Url.OPENAI_CHAT_COMPLETIONS.value))
      .header(Constant.HEADER_CONTENT_TYPE, Constant.CONTENT_JSON)
      .header(Constant.HEADER_AUTHORIZATION, Constant.AUTH_BEARER + apiKey)
      .POST(BodyPublishers.ofString(requestJson))
      .build();
    HttpResponse<Stream<String>> httpResponse = httpClient.send(
      httpRequest, BodyHandlers.ofLines()
    );

    final String CONSUMABLE_TEXT = "\"content\":";
    final int CONSUMABLE_INDEX = 6;
    Stream<ChatResponse> streamChatResponse = httpResponse.body()
      .filter(data -> data.contains(CONSUMABLE_TEXT))
      .map(data -> data.substring(CONSUMABLE_INDEX))
      .map(data -> JsonUtil.one().jsonToObject(data, ChatResponse.class));
		return streamChatResponse;
	}
}