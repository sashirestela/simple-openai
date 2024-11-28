package io.github.sashirestela.openai.support;

public final class Constant {

    private Constant() {
    }

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_AUTHORIZATION = "Bearer ";

    public static final String OPENAI_BASE_URL = "https://api.openai.com";
    public static final String OPENAI_ORG_HEADER = "OpenAI-Organization";
    public static final String OPENAI_PRJ_HEADER = "OpenAI-Project";
    public static final String OPENAI_WS_ENDPOINT_URL = "wss://api.openai.com/v1/realtime";
    public static final String OPENAI_BETA_HEADER = "OpenAI-Beta";
    public static final String OPENAI_ASSISTANT_VERSION = "assistants=v2";
    public static final String OPENAI_REALTIME_VERSION = "realtime=v1";
    public static final String OPENAI_REALTIME_MODEL_NAME = "model";

    public static final String ANYSCALE_BASE_URL = "https://api.endpoints.anyscale.com";

    public static final String AZURE_APIKEY_HEADER = "api-key";
    public static final String AZURE_API_VERSION = "api-version";

}
