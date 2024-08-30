package io.github.sashirestela.openai.domain.assistant;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ThreadRunStepQuery {

    @JsonProperty("step_details.tool_calls[*].file_search.results[*].content")
    FILE_SEARCH_RESULT;

}
