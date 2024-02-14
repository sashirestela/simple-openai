package io.github.sashirestela.openai;

import io.github.sashirestela.cleverclient.http.HttpRequestData;
import java.net.http.HttpClient;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.UnaryOperator;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

/**
 * The factory that generates implementations of the {@link OpenAI OpenAI}
 * interfaces.
 */
@Getter
public class SimpleOpenAIAnyscale extends BaseSimpleOpenAI {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_AUTHORIZATION = "Bearer ";

    /**
     * Constructor used to generate a builder.
     *
     * @param apiKey             Identifier to be used for authentication. Mandatory.
     * @param baseUrl            Host's url, it'll be
     */
    @Builder
    public SimpleOpenAIAnyscale(@NonNull String apiKey, @NonNull String baseUrl) {
        super(apiKey,
            baseUrl,
            Map.of(AUTHORIZATION_HEADER, BEARER_AUTHORIZATION + apiKey),
            null,
            null);
    }
}
