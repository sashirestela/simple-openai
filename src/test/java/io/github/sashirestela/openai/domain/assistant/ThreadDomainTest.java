package io.github.sashirestela.openai.domain.assistant;

import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.domain.DomainTestingHelper;
import io.github.sashirestela.openai.domain.assistant.ThreadRunStepDelta.MessageCreationStepDetail;
import io.github.sashirestela.openai.domain.assistant.ThreadRunStepDelta.ToolCallsStepDetail;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.http.HttpClient;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class ThreadDomainTest {

    static HttpClient httpClient;
    static SimpleOpenAI openAI;
    static String threadId;
    static String fileId;
    static String messageId;
    static String assistantId;
    static String runId;
    static String runStepId;
    static String toolCallId;

    @BeforeAll
    static void setup() {
        httpClient = mock(HttpClient.class);
        openAI = SimpleOpenAI.builder()
                .apiKey("apiKey")
                .httpClient(httpClient)
                .build();
        threadId = "thread_xK2nfHLx8AV4nR2lsC6jHfxo";
        fileId = "file-bI6iyBmUO1jOvZVTd3wLzjFq";
        messageId = "msg_8uvneGIoxEvNfAUyrWxiU6sO";
        assistantId = "asst_HVW5yeBxNVvEMHF5uoFspA2R";
        runId = "run_v34ayOUhnUWSUvDe1TbAQTRh";
        runStepId = "step_8zX8yAgcqQZxTBfLTn8Qp4IG";
        toolCallId = "call_XKj5kxOpCV86xEurhbdvcAFC";
    }

    @Test
    void testThreadsCreate() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/threads__create.json");
        var request = ThreadRequest.builder()
                .message(ThreadMessageRequest.builder()
                        .role(ThreadMessageRequest.Role.USER)
                        .content("What are the order of precedence in artihmetic operations?")
                        .build())
                .build();
        var response = openAI.threads().create(request).join();
        System.out.println(response);
        assertNotNull(response);
    }

    @Test
    void testThreadsModify() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/threads__modify.json");
        var request = ThreadModifyRequest.builder()
                .metadata(Map.of("env", "test"))
                .build();
        var response = openAI.threads().modify(threadId, request).join();
        System.out.println(response);
        assertNotNull(response);
    }

    @Test
    void testThreadsGetOne() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/threads__getone.json");
        var response = openAI.threads().getOne(threadId).join();
        System.out.println(response);
        assertNotNull(response);
    }

    @Test
    void testThreadsDelete() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/threads__delete.json");
        var response = openAI.threads().delete(threadId).join();
        System.out.println(response);
        assertNotNull(response);
    }

    @Test
    void testThreadsMessagesCreate() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/threads_messages_create.json");
        var request = ThreadMessageRequest.builder()
                .role(ThreadMessageRequest.Role.USER)
                .content("What is the product of 123 and 456?")
                .fileId(fileId)
                .build();
        var response = openAI.threads().createMessage(threadId, request).join();
        System.out.println(response);
        assertNotNull(response);
    }

    @Test
    void testThreadsMessagesModify() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/threads_messages_modify.json");
        var request = ThreadMessageModifyRequest.builder()
                .metadata(Map.of("key1", "value1"))
                .build();
        var response = openAI.threads().modifyMessage(threadId, messageId, request).join();
        System.out.println(response);
        assertNotNull(response);
    }

    @Test
    void testThreadsMessagesGetOne() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/threads_messages_getone.json");
        var response = openAI.threads().getMessage(threadId, messageId).join();
        System.out.println(response);
        assertNotNull(response);
    }

    @Test
    void testThreadsMessagesGetList() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/threads_messages_getlist.json");
        var response = openAI.threads().getMessageList(threadId).join();
        System.out.println(response);
        assertNotNull(response);
    }

    @Test
    void testThreadsMessagesDelete() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/threads_messages_delete.json");
        var response = openAI.threads().deleteMessage(threadId, messageId).join();
        System.out.println(response);
        assertNotNull(response);
    }

    @Test
    void testThreadsMessagesFilesGetOne() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/threads_messagesfiles_getone.json");
        var response = openAI.threads().getMessageFile(threadId, messageId, fileId).join();
        System.out.println(response);
        assertNotNull(response);
    }

    @Test
    void testThreadsMessagesFilesGetList() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/threads_messagesfiles_getlist.json");
        var response = openAI.threads().getMessageFileList(threadId, messageId).join();
        System.out.println(response);
        assertNotNull(response);
    }

    @Test
    void testThreadsRunsCreate() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/threads_runs_create.json");
        var request = ThreadRunRequest.builder()
                .assistantId(assistantId)
                .model("gpt-4-1106-preview")
                .instructions("instructions")
                .additionalInstructions("additional Instructions")
                .metadata(Map.of("key1", "value1"))
                .build();
        var response = openAI.threads().createRun(threadId, request).join();
        System.out.println(response);
        assertNotNull(response);
    }

    @Test
    void testThreadsRunsCreateStream() throws IOException {
        DomainTestingHelper.get().mockForStream(httpClient, "src/test/resources/threads_runs_create_stream.txt");
        var request = ThreadRunRequest.builder()
                .assistantId(assistantId)
                .model("gpt-4-1106-preview")
                .instructions("instructions")
                .additionalInstructions("additional Instructions")
                .metadata(Map.of("key1", "value1"))
                .build();
        var response = openAI.threads().createRunStream(threadId, request).join();
        response.filter(e -> e.getName().equals(Events.THREAD_MESSAGE_DELTA))
                .map(e -> ((TextContent) ((ThreadMessageDelta) e.getData()).getDelta().getContent().get(0)).getValue())
                .forEach(System.out::print);
        assertNotNull(response);
    }

    @Test
    void testThreadsRunsModify() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/threads_runs_modify.json");
        var request = ThreadRunModifyRequest.builder()
                .metadata(Map.of("key1", "value1"))
                .build();
        var response = openAI.threads().modifyRun(threadId, runId, request).join();
        System.out.println(response);
        assertNotNull(response);
    }

    @Test
    void testThreadsRunsGetOne() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/threads_runs_getone.json");
        var response = openAI.threads().getRun(threadId, runId).join();
        System.out.println(response);
        assertNotNull(response);
    }

    @Test
    void testThreadsRunsGetList() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/threads_runs_getlist.json");
        var response = openAI.threads().getRunList(threadId).join();
        System.out.println(response);
        assertNotNull(response);
    }

    @Test
    void testThreadsRunsStepsGetOne() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/threads_runssteps_getone.json");
        var response = openAI.threads().getRunStep(threadId, runId, runStepId).join();
        System.out.println(response);
        assertNotNull(response);
    }

    @Test
    void testThreadsRunsStepsGetList() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/threads_runssteps_getlist.json");
        var response = openAI.threads().getRunStepList(threadId, runId).join();
        System.out.println(response);
        assertNotNull(response);
    }

    @Test
    void testThreadsRunsSubmitTool() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/threads_runs_submittool.json");
        var listToolOutputs = List.of(ToolOutput.of(toolCallId, "8.5094"));
        var response = openAI.threads().submitToolOutputs(threadId, runId, listToolOutputs).join();
        System.out.println(response);
        assertNotNull(response);
    }

    @Test
    void testThreadsRunsCreateBoth() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/threads_runs_create_both.json");
        var request = ThreadCreateAndRunRequest.builder()
                .assistantId(assistantId)
                .thread(ThreadMessageList.builder()
                        .message(ThreadMessageRequest.builder()
                                .role(ThreadMessageRequest.Role.USER)
                                .content("What are the order of precedence in artihmetic operations?")
                                .build())
                        .metadata(Map.of("stage", "test"))
                        .build())
                .model("gpt-4-1106-preview")
                .instructions("You are a personal math tutor. Use the added function if necessary or the added files.")
                .tool(AssistantTool.RETRIEVAL)
                .metadata(Map.of("phase", "test"))
                .build();
        var response = openAI.threads().createThreadAndRun(request).join();
        System.out.println(response);
        assertNotNull(response);
    }

    @Test
    void testThreadsRunsCreateBothStream() throws IOException {
        DomainTestingHelper.get().mockForStream(httpClient, "src/test/resources/threads_runs_create_both_stream.txt");
        var request = ThreadCreateAndRunRequest.builder()
                .assistantId(assistantId)
                .thread(ThreadMessageList.builder()
                        .message(ThreadMessageRequest.builder()
                                .role(ThreadMessageRequest.Role.USER)
                                .content(
                                        "Inspect the content of the attached text file. After that plot graph of the formula requested in it.")
                                .build())
                        .metadata(Map.of("stage", "test"))
                        .build())
                .metadata(Map.of("phase", "test"))
                .build();
        var response = openAI.threads().createThreadAndRunStream(request).join();
        response.forEach(e -> {
            switch (e.getName()) {
                case Events.THREAD_RUN_STEP_CREATED:
                    var runStepCreated = (ThreadRunStep) e.getData();
                    System.out.println("\n===== Thread Run Step Created - " + runStepCreated.getType() + " - "
                            + runStepCreated.getId());
                    break;
                case Events.THREAD_RUN_STEP_COMPLETED:
                    var runStepCompleted = (ThreadRunStep) e.getData();
                    System.out.println("\n----- Thread Run Step Completed - " + runStepCompleted.getType() + " - "
                            + runStepCompleted.getId());
                    break;
                case Events.THREAD_RUN_STEP_DELTA:
                    var runStepDeltaDetails = ((ThreadRunStepDelta) e.getData()).getDelta().getStepDetails();
                    if (runStepDeltaDetails instanceof MessageCreationStepDetail) {
                        System.out.println(
                                ((MessageCreationStepDetail) runStepDeltaDetails).getMessageCreation().getMessageId());
                    } else if (runStepDeltaDetails instanceof ToolCallsStepDetail) {
                        var toolCall = ((ToolCallsStepDetail) runStepDeltaDetails).getToolCalls().get(0);
                        if (toolCall.getType().equals("code_interpreter")) {
                            var codeInterpreter = toolCall.getCodeInterpreter();
                            if (codeInterpreter.getInput() != null) {
                                System.out.print(codeInterpreter.getInput());
                            }
                            if (codeInterpreter.getOutputs() != null && codeInterpreter.getOutputs().size() > 0) {
                                var codeInterpreterOutput = codeInterpreter.getOutputs().get(0);
                                if (codeInterpreterOutput.getType().equals("logs")) {
                                    System.out.print("\nOutput Logs = " + codeInterpreterOutput.getLogs());
                                } else if (codeInterpreterOutput.getType().equals("image")) {
                                    System.out.print(
                                            "\nOutput Image File Id = " + codeInterpreterOutput.getImage().getFileId());
                                }
                            }
                        } else if (toolCall.getType().equals("function")) {
                            var functionTool = toolCall.getFunction();
                            if (functionTool.getName() != null) {
                                System.out.println("Function Name = " + functionTool.getName());
                                System.out.print("Function Arguments = ");
                            }
                            if (functionTool.getArguments() != null) {
                                System.out.print(functionTool.getArguments());
                            }
                            if (functionTool.getOutput() != null) {
                                System.out.print("\nFunction Output = " + functionTool.getOutput());
                            }
                        } else if (toolCall.getType().equals("retrieval")) {
                            // Currently OpenAI is replying an empty Map.
                        }
                    }
                    break;
                case Events.THREAD_MESSAGE_DELTA:
                    var messageDeltaFirstContent = ((ThreadMessageDelta) e.getData()).getDelta().getContent().get(0);
                    if (messageDeltaFirstContent instanceof TextContent) {
                        System.out.print(((TextContent) messageDeltaFirstContent).getValue());
                    } else if (messageDeltaFirstContent instanceof ImageFileContent) {
                        System.out.println(
                                "File Id = "
                                        + ((ImageFileContent) messageDeltaFirstContent).getImageFile().getFileId());
                    }
                    break;
                default:
                    break;
            }
        });
        assertNotNull(response);
    }

    @Test
    void testThreadsRunsCancel() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/threads_runs_cancel.json");
        var response = openAI.threads().cancelRun(threadId, runId).join();
        System.out.println(response);
        assertNotNull(response);
    }

}
