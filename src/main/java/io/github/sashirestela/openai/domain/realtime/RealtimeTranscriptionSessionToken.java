package io.github.sashirestela.openai.domain.realtime;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.github.sashirestela.openai.domain.realtime.RealtimeSessionToken.Secret;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RealtimeTranscriptionSessionToken extends RealtimeTranscriptionSession {

    private Secret clientSecret;

    public Secret getClientSecretAtResponse() {
        return clientSecret;
    }

}
