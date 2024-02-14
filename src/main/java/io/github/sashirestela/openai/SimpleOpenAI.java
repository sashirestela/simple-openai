package io.github.sashirestela.openai;

import io.github.sashirestela.cleverclient.http.HttpRequestData;
import java.net.http.HttpClient;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.UnaryOperator;
import lombok.Builder;
import lombok.Getter;

/**
 * The factory that generates implementations of the {@link OpenAI OpenAI}
 * interfaces.
 */
@Getter
public class SimpleOpenAI extends BaseSimpleOpenAI {

    public static final String OPENAI_BASE_URL = "https://api.openai.com";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String ORGANIZATION_HEADER = "OpenAI-Organization";
    private static final String BEARER_AUTHORIZATION = "Bearer ";

    private static Map<String, String> prepareHeaders(String apiKey, String organizationId) {
        var headers = new HashMap<String, String>();
        headers.put(AUTHORIZATION_HEADER, BEARER_AUTHORIZATION + apiKey);

        if (organizationId != null) {
            headers.put(ORGANIZATION_HEADER, organizationId);
        }

        return headers;
    }


    /**
     * Constructor used to generate a builder.
     *
     * @param apiKey             Identifier to be used for authentication. Mandatory.
     * @param organizationId     Organization's id to be charged for usage. Optional.
     * @param baseUrl            Host's url, it'll be
     *                           <a href="https://api.openai.com">...</a>. Optional.
     * @param httpClient         A {@link java.net.http.HttpClient HttpClient} object.
     *                           One is created by default if not provided. Optional.
     * @param requestInterceptor A function to intercept and modify the request
     */
    @Builder
    public SimpleOpenAI(
            String apiKey,
            String organizationId,
            String baseUrl,
            HttpClient httpClient,
            UnaryOperator<HttpRequestData> requestInterceptor) {
        super(apiKey,
              Optional.ofNullable(baseUrl).orElse(OPENAI_BASE_URL),
              prepareHeaders(apiKey, organizationId),
              httpClient,
              requestInterceptor);
    }
}
