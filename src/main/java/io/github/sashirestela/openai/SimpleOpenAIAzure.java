package io.github.sashirestela.openai;

import io.github.sashirestela.cleverclient.http.HttpRequestData;
import io.github.sashirestela.cleverclient.support.ContentType;
import java.net.http.HttpClient;
import java.util.Map;
import java.util.function.UnaryOperator;
import lombok.Builder;
import lombok.NonNull;

/**
 * The factory that generates implementations of the {@link OpenAI OpenAI}
 * interfaces.
 */

public class SimpleOpenAIAzure extends BaseSimpleOpenAI {

    private static BaseSimpleOpenAiArgs prepareBaseSimpleOpenAiArgs(
        String apiKey, String baseUrl, String apiVersion, HttpClient httpClient) {

        var headers = Map.of("api-Key", apiKey);

        // Inline the UnaryOperator<HttpRequestData> as a lambda directly.
        var requestInterceptor = (UnaryOperator<HttpRequestData>) request -> {
            var url = request.getUrl();
            var contentType = request.getContentType();
            var body = request.getBody();

            // add a query parameter to url
            url += (url.contains("?") ? "&" : "?") + "api-version=" + apiVersion;
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
                    var bodyMap = (Map<String, Object>) request.getBody();
                    // remove a field from body (as Map)
                    bodyMap.remove("model");
                    body = bodyMap;
                }
                request.setBody(body);
            }

            return request;
        };

        return BaseSimpleOpenAiArgs.builder()
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
        super(prepareBaseSimpleOpenAiArgs(apiKey, baseUrl, apiVersion, httpClient));
    }
}
