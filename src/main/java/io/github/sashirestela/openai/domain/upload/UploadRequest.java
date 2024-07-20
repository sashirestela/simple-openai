package io.github.sashirestela.openai.domain.upload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.github.sashirestela.openai.domain.file.FileRequest.PurposeType;
import io.github.sashirestela.slimvalidator.constraints.Range;
import io.github.sashirestela.slimvalidator.constraints.Required;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UploadRequest {

    @Required
    private String filename;

    @Required
    private PurposeType purpose;

    @Required
    @Range(max = 8_589_934_592L)
    private Long bytes;

    @Required
    private String mimeType;

}
