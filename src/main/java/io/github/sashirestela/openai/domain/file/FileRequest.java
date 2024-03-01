package io.github.sashirestela.openai.domain.file;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.nio.file.Path;

@Getter
@Builder
public class FileRequest {

    @NonNull
    private Path file;
    @NonNull
    private PurposeType purpose;

}
