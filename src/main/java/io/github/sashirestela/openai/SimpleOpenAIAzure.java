package io.github.sashirestela.openai;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sashirestela.cleverclient.http.HttpRequestData;
import io.github.sashirestela.cleverclient.support.ContentType;
import io.github.sashirestela.openai.base.ClientConfig;
import io.github.sashirestela.openai.base.OpenAIConfigurator;
import io.github.sashirestela.openai.base.OpenAIProvider;
import io.github.sashirestela.openai.service.ChatCompletionServices;
import io.github.sashirestela.openai.service.FileServices;
import io.github.sashirestela.openai.support.Constant;
import lombok.Builder;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

import java.net.http.HttpClient;
import java.util.Map;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

/**
 * The Azure OpenAI implementation which implements a subset of the standard services.
 */
public class SimpleOpenAIAzure extends OpenAIProvider implements
        ChatCompletionServices,
        FileServices {

    /**
     * Constructor used to generate a builder.
     *
     * @param apiKey       Identifier to be used for authentication. Mandatory.
     * @param baseUrl      The URL of the Azure OpenAI deployment. Mandatory.
     * @param apiVersion   Azure OpenAI API version. See: <a href=
     *                     "https://learn.microsoft.com/en-us/azure/ai-services/openai/reference#rest-api-versioning">Azure
     *                     OpenAI API versioning</a>. Mandatory.
     * @param httpClient   A {@link HttpClient HttpClient} object. One is created by default if not
     *                     provided. Optional.
     * @param objectMapper Provides Json conversions either to and from objects. Optional.
     */
    @Builder
    public SimpleOpenAIAzure(@NonNull String apiKey, @NonNull String baseUrl, @NonNull String apiVersion,
            HttpClient httpClient, ObjectMapper objectMapper) {
        super(AzureConfigurator.builder()
                .apiKey(apiKey)
                .baseUrl(baseUrl)
                .apiVersion(apiVersion)
                .httpClient(httpClient)
                .objectMapper(objectMapper)
                .build());
    }

    @Override
    public OpenAI.ChatCompletions chatCompletions() {
        return getOrCreateService(OpenAI.ChatCompletions.class);
    }

    @Override
    public OpenAI.Files files() {
        return getOrCreateService(OpenAI.Files.class);
    }

    @SuperBuilder
    static class AzureConfigurator extends OpenAIConfigurator {

        private String apiVersion;

        @Override
        public ClientConfig buildConfig() {
            return ClientConfig.builder()
                    .baseUrl(baseUrl)
                    .headers(Map.of(Constant.AZURE_APIKEY_HEADER, apiKey))
                    .httpClient(httpClient)
                    .requestInterceptor(makeRequestInterceptor())
                    .objectMapper(objectMapper)
                    .build();
        }

        private UnaryOperator<HttpRequestData> makeRequestInterceptor() {
            return request -> {
                var url = request.getUrl();
                var contentType = request.getContentType();
                if (contentType != null) {
                    var body = makeNewBody(request, contentType, url);
                    request.setBody(body);
                }
                url = makeNewUrl(url, apiVersion);
                request.setUrl(url);

                return request;
            };
        }

        @SuppressWarnings("unchecked")
        private Object makeNewBody(HttpRequestData request, ContentType contentType, String url) {
            var deployment = extractDeployment(url);
            var body = request.getBody();
            if (contentType.equals(ContentType.APPLICATION_JSON)) {
                return getBodyForJson(url, (String) body, deployment);
            } else {
                return getBodyForMap(url, (Map<String, Object>) body, deployment);
            }
        }

        private String makeNewUrl(String url, String apiVersion) {
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

        private String extractDeployment(String url) {
            final String DEPLOYMENT_REGEX = "/deployments/([^/]+)/";

            var pattern = Pattern.compile(DEPLOYMENT_REGEX);
            var matcher = pattern.matcher(url);

            if (matcher.find()) {
                return matcher.group(1);
            } else {
                return null;
            }
        }

        private Object getBodyForJson(String url, String body, String deployment) {
            final String MODEL_ENTRY_REGEX = "\"model\"\\s*:\\s*\"[^\"]+\"\\s*,?\\s*";
            final String TRAILING_COMMA_REGEX = ",\\s*}";
            final String CLOSING_BRACE = "}";
            final String MODEL_LITERAL = "model";
            final String ASSISTANTS_LITERAL = "/assistants";

            var model = "";
            if (url.contains(ASSISTANTS_LITERAL)) {
                model = "\"" + MODEL_LITERAL + "\":\"" + deployment + "\",";
            }
            body = body.replaceFirst(MODEL_ENTRY_REGEX, model);
            body = body.replaceFirst(TRAILING_COMMA_REGEX, CLOSING_BRACE);

            return body;
        }

        private Object getBodyForMap(String url, Map<String, Object> body, String deployment) {
            final String ASSISTANTS_LITERAL = "/assistants";
            final String MODEL_LITERAL = "model";

            if (url.contains(ASSISTANTS_LITERAL)) {
                body.put(MODEL_LITERAL, deployment);
            } else {
                body.remove(MODEL_LITERAL);
            }

            return body;
        }

    }

}
