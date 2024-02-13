package io.github.sashirestela.openai.demo;


import io.github.sashirestela.cleverclient.support.ContentType;
import io.github.sashirestela.openai.domain.chat.ChatRequest;
import io.github.sashirestela.openai.domain.chat.message.ChatMsgSystem;
import io.github.sashirestela.openai.domain.chat.message.ChatMsgUser;
import java.util.Map;
import java.util.Optional;

public class AzureOpenAIChatServiceDemo extends AbstractDemo {
    private static final String AZURE_OPENAI_API_KEY_HEADER = "api-key";
    private final ChatRequest chatRequest;

    @SuppressWarnings("unchecked")
    public AzureOpenAIChatServiceDemo(String baseUrl, String apiKey, String model) {
        super(baseUrl, apiKey, request -> {
            var url = request.getUrl();
            var contentType = request.getContentType();
            var body = request.getBody();

            // add a header to the request
            var headers = request.getHeaders();
            headers.put(AZURE_OPENAI_API_KEY_HEADER, apiKey);
            request.setHeaders(headers);

            // add a query parameter to url
            url += (url.contains("?") ? "&" : "?") + "api-version=2023-05-15";
            // remove '/vN' or '/vN.M' from url
            url = url.replaceFirst("(\\/v\\d+\\.*\\d*)", "");
            request.setUrl(url);

            if (contentType != null) {
                if (contentType.equals(ContentType.APPLICATION_JSON)) {
                    var bodyJson = (String) request.getBody();
                    // remove a field from body (as Json)
                    bodyJson = bodyJson.replaceFirst(",?\"model\":\"[^\"]*\",?", "");
                    bodyJson = bodyJson.replaceFirst("\"\"", "\",\"");
                    body = bodyJson;
                }
                if (contentType.equals(ContentType.MULTIPART_FORMDATA)) {
                    Map<String, Object> bodyMap = (Map<String, Object>) request.getBody();
                    // remove a field from body (as Map)
                    bodyMap.remove("model");
                    body = bodyMap;
                }
                request.setBody(body);
            }

            return request;
        });

        chatRequest = ChatRequest.builder()
            .model(model)
            .message(new ChatMsgSystem("You are an expert in AI."))
            .message(
                new ChatMsgUser("Write a technical article about ChatGPT, no more than 100 words."))
            .temperature(0.0)
            .maxTokens(300)
            .build();
    }

    public void demoCallChatBlocking() {
        var futureChat = openAI.chatCompletions().create(chatRequest);
        var chatResponse = futureChat.join();
        System.out.println(chatResponse.firstContent());
    }

    public static void main(String[] args) {
        var baseUrl = System.getenv("CUSTOM_OPENAI_BASE_URL");
        var apiKey = System.getenv("CUSTOM_OPENAI_API_KEY");
        // Services like Azure OpenAI don't require a model (endpoints have built-in model)
        var model = Optional.ofNullable(System.getenv("CUSTOM_OPENAI_MODEL"))
            .orElse("N/A");
        var demo = new AzureOpenAIChatServiceDemo(baseUrl, apiKey, model);

        demo.addTitleAction("Call Completion (Blocking Approach)", demo::demoCallChatBlocking);

        demo.run();
    }
}
