package io.github.sashirestela.openai.domain.assistant;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Singular;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Attachment {

    private String fileId;

    @Singular
    private List<AttachmentTool> tools;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @ToString
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class AttachmentTool {

        public static final AttachmentTool CODE_INTERPRETER = new AttachmentTool(ToolType.CODE_INTERPRETER);
        public static final AttachmentTool FILE_SEARCH = new AttachmentTool(ToolType.FILE_SEARCH);

        private ToolType type;

    }

}
