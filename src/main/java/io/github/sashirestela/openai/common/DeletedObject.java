package io.github.sashirestela.openai.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
public class DeletedObject {

    private String id;
    private String object;
    private Boolean deleted;

}
