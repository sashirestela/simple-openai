package io.github.sashirestela.openai;

import io.github.sashirestela.cleverclient.http.HttpRequestData;
import io.github.sashirestela.cleverclient.support.ContentType;
import java.net.http.HttpClient;
import java.util.Map;
import java.util.function.UnaryOperator;
import lombok.Builder;
import lombok.NonNull;

/**
 * This class provides the chatCompletion() service for the Azure OpenAI provider
 * Note that each instance of SimpleOpenAIAzure is linked to a single specific model.
 * The capabilities of the model determine which chatCompletion() methods are available.
 */
public class SimpleOpenAIAzure extends BaseSimpleOpenAI {

    public static final String API_KEY_HEADER = "api-key";
    public static final String API_VERSION = "api-version";

    private static final String ENDPOINT_VERSION_REGEX = "(\\/v\\d+\\.*\\d*)";
    private static final String MODEL_REGEX =  ",?\"model\":\"[^\"]*\",?";

    private static final String EMPTY_REGEX = "\"\"";
    private static final String QUOTED_COMMA = "\",\"";

    private static final String MODEL_LITERAL = "model";

    public static BaseSimpleOpenAIArgs prepareBaseSimpleOpenAIArgs(String apiKey, String baseUrl, String apiVersion, HttpClient httpClient) {

        var headers = Map.of(API_KEY_HEADER, apiKey);

        var requestInterceptor = (UnaryOperator<HttpRequestData>) request -> {
            var url = request.getUrl();
            var contentType = request.getContentType();
            var body = request.getBody();

            url += (url.contains("?") ? "&" : "?") + API_VERSION + "=" + apiVersion;
            url = url.replaceFirst(ENDPOINT_VERSION_REGEX, "");
            request.setUrl(url);

            if (contentType != null) {
                if (contentType.equals(ContentType.APPLICATION_JSON)) {
                    var bodyJson = (String) request.getBody();
                    bodyJson = bodyJson.replaceFirst(MODEL_REGEX, "");
                    bodyJson = bodyJson.replaceFirst(EMPTY_REGEX, QUOTED_COMMA);
                    body = bodyJson;
                }
                if (contentType.equals(ContentType.MULTIPART_FORMDATA)) {
                    @SuppressWarnings("unchecked")
                    var bodyMap = (Map<String, Object>) request.getBody();
                    bodyMap.remove(MODEL_LITERAL);
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
     * Constructor used to generate a builder.
     *
     * @param apiKey         Identifier to be used for authentication. Mandatory.
     * @param baseUrl        The URL of the Azure OpenAI deployment.   Mandatory.
     * @param apiVersion     Azure OpenAI API version. See:
     *                       <a href="https://learn.microsoft.com/en-us/azure/ai-services/openai/reference#rest-api-versioning">Azure OpenAI API versioning</a>
     * @param httpClient     A {@link HttpClient HttpClient} object.
     *                       One is created by default if not provided. Optional.
     */
    @Builder
    public SimpleOpenAIAzure(
        @NonNull String apiKey,
        @NonNull String baseUrl,
        @NonNull String apiVersion,
        HttpClient httpClient) {
        super(prepareBaseSimpleOpenAIArgs(apiKey, baseUrl, apiVersion, httpClient));
    }
}
