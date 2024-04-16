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

    private static final String NOT_IMPLEMENTED = "Not implemented.";
    private OpenAI.Files fileService;
    private OpenAIBeta.Assistants assistantService;
    private OpenAIBeta.Threads threadService;

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
            return matcher.group(1);
        } else {
            return null;
        }
    }

    private static String getNewUrl(String url, String apiVersion) {
        final String VERSION_REGEX = "(/v\\d+\\.*\\d*)";
        final String DEPLOYMENT_REGEX = "/deployments/[^/]+/";
        final String CHAT_COMPLETIONS_LITERAL = "/chat/completions";

        url += (url.contains("?") ? "&" : "?") + Constant.AZURE_API_VERSION + "=" + apiVersion;
        url = url.replaceFirst(VERSION_REGEX, "");

        // Strip deployment from URL unless it's /chat/completions call
        if (!url.contains(CHAT_COMPLETIONS_LITERAL)) {
            url = url.replaceFirst(DEPLOYMENT_REGEX, "/");
        }

        return url;
    }

    private static Object getBodyForJson(String url, String body, String deployment) {
        final String MODEL_REGEX = ",?\"model\":\"[^\"]*\",?";
        final String EMPTY_REGEX = "\"\"";
        final String QUOTED_COMMA = "\",\"";
        final String MODEL_LITERAL = "model";
        final String ASSISTANTS_LITERAL = "/assistants";

        var model = "";
        if (url.contains(ASSISTANTS_LITERAL)) {
            model = "\"" + MODEL_LITERAL + "\":\"" + deployment + "\"";
        }
        body = body.replaceFirst(MODEL_REGEX, model);
        body = body.replaceFirst(EMPTY_REGEX, QUOTED_COMMA);

        return body;
    }

    private static Object getBodyForMap(String url, Map<String, Object> body, String deployment) {
        final String ASSISTANTS_LITERAL = "/assistants";
        final String MODEL_LITERAL = "model";

        if (url.contains(ASSISTANTS_LITERAL)) {
            body.put(MODEL_LITERAL, deployment);
        } else {
            body.remove(MODEL_LITERAL);
        }
        return body;
    }

    @SuppressWarnings("unchecked")
    private static void updateRequestBody(HttpRequestData request, ContentType contentType, String url) {
        var deployment = extractDeployment(url);
        var body = request.getBody();
        if (contentType.equals(ContentType.APPLICATION_JSON)) {
            body = getBodyForJson(url, (String) body, deployment);
        } else {
            body = getBodyForMap(url, (Map<String, Object>) body, deployment);
        }
        request.setBody(body);
    }

    public static BaseSimpleOpenAIArgs prepareBaseSimpleOpenAIArgs(String apiKey, String baseUrl, String apiVersion,
            HttpClient httpClient) {

        var headers = Map.of(Constant.AZURE_APIKEY_HEADER, apiKey);
        UnaryOperator<HttpRequestData> requestInterceptor = request -> {
            var url = request.getUrl();
            var contentType = request.getContentType();
            if (contentType != null) {
                updateRequestBody(request, contentType, url);
            }
            url = getNewUrl(url, apiVersion);
            request.setUrl(url);

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
     * Generates an implementation of the Assistant interface to handle requests.
     *
     * @return An instance of the interface. It is created only once.
     */
    @Override
    public OpenAIBeta.Assistants assistants() {
        if (assistantService == null) {
            assistantService = cleverClient.create(OpenAIBeta.Assistants.class);
        }
        return assistantService;
    }

    /**
     * Throw not implemented
     */
    @Override
    public OpenAI.Audios audios() {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    /**
     * Throw not implemented
     */
    @Override
    public OpenAI.Completions completions() {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    /**
     * Throw not implemented
     */
    @Override
    public OpenAI.Embeddings embeddings() {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    /**
     * Generates an implementation of the Files interface to handle requests.
     *
     * @return An instance of the interface. It is created only once.
     */
    @Override
    public OpenAI.Files files() {
        if (fileService == null) {
            fileService = cleverClient.create(OpenAI.Files.class);
        }
        return fileService;
    }

    /**
     * Throw not implemented
     */
    @Override
    public OpenAI.FineTunings fineTunings() {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    /**
     * Throw not implemented
     */
    @Override
    public OpenAI.Images images() {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    /**
     * Throw not implemented
     */
    @Override
    public OpenAI.Models models() {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    /**
     * Throw not implemented
     */
    @Override
    public OpenAI.Moderations moderations() {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    /**
     * Spawns a single instance of the Threads interface to manage requests.
     *
     * @return An instance of the interface. It is created only once.
     */
    @Override
    public OpenAIBeta.Threads threads() {
        if (threadService == null) {
            threadService = cleverClient.create(OpenAIBeta.Threads.class);
        }
        return threadService;
    }

}
