package io.github.sashirestela.openai;

import io.github.sashirestela.cleverclient.CleverClient;
import io.github.sashirestela.cleverclient.http.HttpRequestData;
import java.net.http.HttpClient;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.UnaryOperator;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * The factory that generates implementations of the {@link OpenAI OpenAI}
 * interfaces.
 */

public interface BaseSimpleOpenAI {

    /**
     * Generates an implementation of the Audios interface to handle requests.
     *
     * @return An instance of the interface. It is created only once.
     */
    public OpenAI.Audios audios();

    /**
     * Generates an implementation of the ChatCompletions interface to handle
     * requests.
     *
     * @return An instance of the interface. It is created only once.
     */
    public OpenAI.ChatCompletions chatCompletions();

    /**
     * Generates an implementation of the Completions interface to handle requests.
     *
     * @return An instance of the interface. It is created only once.
     */
    public OpenAI.Completions completions();

    /**
     * Generates an implementation of the Embeddings interface to handle requests.
     *
     * @return An instance of the interface. It is created only once.
     */
    public OpenAI.Embeddings embeddings();

    /**
     * Generates an implementation of the Files interface to handle requests.
     *
     * @return An instance of the interface. It is created only once.
     */
    public OpenAI.Files files();

    /**
     * Generates an implementation of the FineTunings interface to handle requests.
     *
     * @return An instance of the interface. It is created only once.
     */
    public OpenAI.FineTunings fineTunings();

    /**
     * Generates an implementation of the Images interface to handle requests.
     *
     * @return An instance of the interface. It is created only once.
     */
    public OpenAI.Images images();

    /**
     * Generates an implementation of the Models interface to handle requests.
     *
     * @return An instance of the interface. It is created only once.
     */
    public OpenAI.Models models();

    /**
     * Generates an implementation of the Moderations interface to handle requests.
     *
     * @return An instance of the interface. It is created only once.
     */
    public OpenAI.Moderations moderations();

    /**
     * Generates an implementation of the Assistant interface to handle requests.
     *
     * @return An instance of the interface. It is created only once.
     */
    public OpenAI.Assistants assistants();

    /**
     * Spawns a single instance of the Threads interface to manage requests.
     *
     * @return An instance of the interface. It is created only once.
     */
    public OpenAI.Threads threads();
}
