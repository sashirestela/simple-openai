package io.github.sashirestela.openai.domain.assistant;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.github.sashirestela.openai.common.tool.ToolType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = StepDetail.MessageCreationStep.class, name = "message_creation"),
        @JsonSubTypes.Type(value = StepDetail.ToolCallsStep.class, name = "tool_calls")
})
@Getter
public class StepDetail {

    protected RunStepType type;

    @NoArgsConstructor
    @Getter
    @ToString
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class MessageCreationStep extends StepDetail {

        private MessageCreation messageCreation;

        @NoArgsConstructor
        @Getter
        @ToString
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class MessageCreation {

            private String messageId;

        }

    }

    @NoArgsConstructor
    @Getter
    @ToString
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ToolCallsStep extends StepDetail {

        private List<StepToolCall> toolCalls;

        @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type",
                defaultImpl = StepToolCall.UnknownToolCall.class)
        @JsonSubTypes({
                @JsonSubTypes.Type(value = StepToolCall.CodeInterpreterToolCall.class, name = "code_interpreter"),
                @JsonSubTypes.Type(value = StepToolCall.FileSearchToolCall.class, name = "file_search"),
                @JsonSubTypes.Type(value = StepToolCall.FunctionToolCall.class, name = "function")
        })
        @Getter
        public abstract static class StepToolCall {

            protected Integer index;
            protected String id;
            protected ToolType type;

            @NoArgsConstructor
            @Getter
            @ToString
            @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
            public static class CodeInterpreterToolCall extends StepToolCall {

                private CodeInterpreterTool codeInterpreter;

                @NoArgsConstructor
                @Getter
                @ToString
                @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
                public static class CodeInterpreterTool {

                    private String input;
                    private List<Output> outputs;

                    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY,
                            property = "type")
                    @JsonSubTypes({
                            @JsonSubTypes.Type(value = Output.LogOutput.class, name = "logs"),
                            @JsonSubTypes.Type(value = Output.ImageOutput.class, name = "image")
                    })
                    @Getter
                    public abstract static class Output {

                        protected Integer index;
                        protected OutputType type;

                        @NoArgsConstructor
                        @Getter
                        @ToString
                        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
                        public static class LogOutput extends Output {

                            private String logs;

                        }

                        @NoArgsConstructor
                        @Getter
                        @ToString
                        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
                        public static class ImageOutput extends Output {

                            private ImageOutputFile image;

                            @NoArgsConstructor
                            @Getter
                            @ToString
                            @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
                            public static class ImageOutputFile {

                                private String fileId;

                            }

                        }

                    }

                    public enum OutputType {

                        @JsonProperty("logs")
                        LOGS,

                        @JsonProperty("image")
                        IMAGE;

                    }

                }

            }

            @NoArgsConstructor
            @Getter
            @ToString
            @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
            public static class FileSearchToolCall extends StepToolCall {

                private FileSearch fileSearch;

                @NoArgsConstructor
                @Getter
                @ToString
                @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
                public static class FileSearch {

                    private RankingOption rankingOptions;
                    private List<FileSearchResult> results;

                    @NoArgsConstructor
                    @Getter
                    @ToString
                    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
                    public static class FileSearchResult {

                        private String fileId;
                        private String fileName;
                        private Double score;
                        private List<FileSearchContent> content;

                        @NoArgsConstructor
                        @Getter
                        @ToString
                        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
                        public static class FileSearchContent {

                            private String type;
                            private String text;

                        }

                    }

                }

            }

            @NoArgsConstructor
            @Getter
            @ToString
            @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
            public static class FunctionToolCall extends StepToolCall {

                private FunctionTool function;

                @NoArgsConstructor
                @Getter
                @ToString
                @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
                public static class FunctionTool {

                    private String name;
                    private String arguments;
                    private String output;

                }

            }

            @NoArgsConstructor
            @Getter
            @ToString
            @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
            public static class UnknownToolCall extends StepToolCall {
            }

        }

    }

}
