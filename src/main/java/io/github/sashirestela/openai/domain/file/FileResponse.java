package io.github.sashirestela.openai.domain.file;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
public class FileResponse {

    private String id;

    private String object;

    private String purpose;

    private String filename;

    private Integer bytes;

    @JsonProperty("created_at")
    private Long createdAt;

    @Deprecated
    private String status;

    @Deprecated
    @JsonProperty("status_details")
    private String statusDetails;

}