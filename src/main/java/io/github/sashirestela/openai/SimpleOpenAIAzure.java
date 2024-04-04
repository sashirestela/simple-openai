package io.github.sashirestela.openai;

import io.github.sashirestela.cleverclient.http.HttpRequestData;
import io.github.sashirestela.cleverclient.support.ContentType;
import io.github.sashirestela.openai.support.Constant;
import lombok.Builder;
import lombok.NonNull;

import java.net.http.HttpClient;
import java.util.Map;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

/**
 * This class provides the chatCompletion() service for the Azure OpenAI provider. Note that each
 * instance of SimpleOpenAIAzure is linked to a single specific model. The capabilities of the model
 * determine which chatCompletion() methods are available.
 */
public class SimpleOpenAIAzure extends BaseSimpleOpenAI {

    private OpenAI.Files fileService;
    private OpenAI.Assistants assistantService;
    private OpenAI.Threads threadService;

    /**
     * Constructor used to generate a builder.
     *
     * @param apiKey     Identifier to be used for authentication. Mandatory.
     * @param baseUrl    The URL of the Azure OpenAI deployment. Mandatory.
     * @param apiVersion Azure OpenAI API version. See: <a href=
     *                   "https://learn.microsoft.com/en-us/azure/ai-services/openai/reference#rest-api-versioning">Azure
     *                   OpenAI API versioning</a>. Mandatory.
     * @param httpClient A {@link HttpClient HttpClient} object. One is created by default if not
     *                   provided. Optional.
     */
    @Builder
    public SimpleOpenAIAzure(@NonNull String apiKey, @NonNull String baseUrl, @NonNull String apiVersion,
            HttpClient httpClient) {
        super(prepareBaseSimpleOpenAIArgs(apiKey, baseUrl, apiVersion, httpClient));
    }

    private static String extractDeployment(String url) {
        final String DEPLOYMENT_REGEX = "/deployments/([^/]+)/";

        var pattern = Pattern.compile(DEPLOYMENT_REGEX);
        var matcher = pattern.matcher(url);

        if (matcher.find()) {
            return matcher.group(1); // Return the first matched group
        }

        return null; // Return null if no match was found
    }

    public static BaseSimpleOpenAIArgs prepareBaseSimpleOpenAIArgs(String apiKey, String baseUrl, String apiVersion,
            HttpClient httpClient) {

        var headers = Map.of(Constant.AZURE_APIKEY_HEADER, apiKey);
        UnaryOperator<HttpRequestData> requestInterceptor = request -> {
            final String VERSION_REGEX = "(\\/v\\d+\\.*\\d*)";
            final String MODEL_REGEX = ",?\"model\":\"[^\"]*\",?";
            final String EMPTY_REGEX = "\"\"";
            final String QUOTED_COMMA = "\",\"";
            final String MODEL_LITERAL = "model";

            var url = request.getUrl();
            var contentType = request.getContentType();
            var body = request.getBody();

            var deployment = extractDeployment(url);

            url += (url.contains("?") ? "&" : "?") + Constant.AZURE_API_VERSION + "=" + apiVersion;
            url = url.replaceFirst(VERSION_REGEX, "");

            // Strip deployment from URL unless it's /chat/completions call
            if (!url.contains("/chat/completions")) {
                url = url.replaceFirst("/deployments/[^/]+/", "/");
            }

            request.setUrl(url);

            if (contentType != null) {
                if (contentType.equals(ContentType.APPLICATION_JSON)) {
                    var bodyJson = (String) request.getBody();
                    var model = "";
                    if (url.contains("/assistants")) {
                        model = "\"" + MODEL_LITERAL + "\":\"" + deployment + "\"";
                    }
                    bodyJson = bodyJson.replaceFirst(MODEL_REGEX, model);
                    bodyJson = bodyJson.replaceFirst(EMPTY_REGEX, QUOTED_COMMA);
                    body = bodyJson;
                }
                if (contentType.equals(ContentType.MULTIPART_FORMDATA)) {
                    @SuppressWarnings("unchecked")
                    var bodyMap = (Map<String, Object>) request.getBody();
                    if (url.contains("/assistants")) {
                        bodyMap.put(MODEL_LITERAL, deployment);
                    } else {
                        bodyMap.remove(MODEL_LITERAL);
                    }
                    body = bodyMap;
                }
                request.setBody(body);
            }

            return request;
        };

        return BaseSimpleOpenAIArgs.builder()
                .baseUrl(baseUrl)
                .headers(headers)
                .httpClient(httpClient)
                .requestInterceptor(requestInterceptor)
                .build();
    }

    /**
     * Generates an implementation of the Files interface to handle requests.
     *
     * @return An instance of the interface. It is created only once.
     */
    public OpenAI.Files files() {
        if (fileService == null) {
            fileService = cleverClient.create(OpenAI.Files.class);
        }
        return fileService;
    }

    /**
     * Generates an implementation of the Assistant interface to handle requests.
     *
     * @return An instance of the interface. It is created only once.
     */
    public OpenAI.Assistants assistants() {
        if (assistantService == null) {
            assistantService = cleverClient.create(OpenAI.Assistants.class);
        }
        return assistantService;
    }

    /**
     * Spawns a single instance of the Threads interface to manage requests.
     *
     * @return An instance of the interface. It is created only once.
     */
    public OpenAI.Threads threads() {
        if (threadService == null) {
            threadService = cleverClient.create(OpenAI.Threads.class);
        }
        return threadService;
    }

}
