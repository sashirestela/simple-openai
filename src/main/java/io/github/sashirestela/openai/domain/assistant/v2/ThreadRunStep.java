package io.github.sashirestela.openai.domain.assistant.v2;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.github.sashirestela.openai.domain.OpenAIUsage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
@Getter
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ThreadRunStep {

    private String id;
    private String object;
    private Integer createdAt;
    private String assistantId;
    private String threadId;
    private String runId;
    private RunStepType type;
    private RunStepStatus status;
    private StepDetail stepDetails;
    private ThreadRun.LastError lastError;
    private Integer expiredAt;
    private Integer cancelledAt;
    private Integer failedAt;
    private Integer completedAt;
    private Map<String, String> metadata;
    private OpenAIUsage usage;

    public enum RunStepType {

        @JsonProperty("message_creation")
        MESSAGE_CREATION,

        @JsonProperty("tool_calls")
        TOOL_CALLS;

    }

    public enum RunStepStatus {

        @JsonProperty("in_progress")
        IN_PROGRESS,

        @JsonProperty("cancelled")
        CANCELLED,

        @JsonProperty("failed")
        FAILED,

        @JsonProperty("completed")
        COMPLETED,

        @JsonProperty("expired")
        EXPIRED;

    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
    @JsonSubTypes({
            @JsonSubTypes.Type(value = StepDetail.MessageCreationStep.class, name = "message_creation"),
            @JsonSubTypes.Type(value = StepDetail.ToolCallsStep.class, name = "tool_calls")
    })
    @Getter
    public static class StepDetail {

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

            private List<ToolCall> toolCalls;

            @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
            @JsonSubTypes({
                    @JsonSubTypes.Type(value = ToolCall.CodeInterpreterToolCall.class, name = "code_interpreter"),
                    @JsonSubTypes.Type(value = ToolCall.FileSearchToolCall.class, name = "file_search"),
                    @JsonSubTypes.Type(value = ToolCall.FunctionToolCall.class, name = "function")
            })
            @Getter
            public static class ToolCall {

                protected Integer index;
                protected String id;
                protected ToolType type;

                @NoArgsConstructor
                @Getter
                @ToString
                @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
                public static class CodeInterpreterToolCall extends ToolCall {

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
                        public static class Output {

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
                public static class FileSearchToolCall extends ToolCall {

                    private Map<String, String> fileSearch;

                }

                @NoArgsConstructor
                @Getter
                @ToString
                @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
                public static class FunctionToolCall extends ToolCall {

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

            }

        }

    }

}
