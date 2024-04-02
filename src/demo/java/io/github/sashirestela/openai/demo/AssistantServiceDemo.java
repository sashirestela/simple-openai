package io.github.sashirestela.openai.demo;

import io.github.sashirestela.openai.domain.assistant.AssistantRequest;
import io.github.sashirestela.openai.domain.assistant.AssistantTool;
import io.github.sashirestela.openai.domain.assistant.Events;
import io.github.sashirestela.openai.domain.assistant.ImageFileContent;
import io.github.sashirestela.openai.domain.assistant.TextContent;
import io.github.sashirestela.openai.domain.assistant.ThreadMessage;
import io.github.sashirestela.openai.domain.assistant.ThreadMessageDelta;
import io.github.sashirestela.openai.domain.assistant.ThreadMessageRequest;
import io.github.sashirestela.openai.domain.assistant.ThreadRequest;
import io.github.sashirestela.openai.domain.assistant.ThreadRun;
import io.github.sashirestela.openai.domain.assistant.ThreadRunRequest;
import io.github.sashirestela.openai.domain.assistant.ThreadRunStep;
import io.github.sashirestela.openai.domain.assistant.ThreadRunStepDelta;
import io.github.sashirestela.openai.domain.assistant.ThreadRunStepDelta.MessageCreationStepDetail;
import io.github.sashirestela.openai.domain.assistant.ThreadRunStepDelta.ToolCallsStepDetail;
import io.github.sashirestela.openai.domain.file.FileRequest;
import io.github.sashirestela.openai.domain.file.PurposeType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class AssistantServiceDemo extends AbstractDemo {

    String assistantId;
    String fileId;
    String threadId;
    String runId;

    public void demoCreateAssistant() {
        var assistantRequest = AssistantRequest.builder()
                .model("gpt-3.5-turbo-1106")
                .build();

        var assistant = openAI.assistants().create(assistantRequest).join();
        System.out.println(assistant);
        assistantId = assistant.getId();
    }

    public void demoRetrieveAndModifyAssistant() {
        var assistant = openAI.assistants().getOne(assistantId).join();
        var assistantRequest = assistant.mutate()
                .name("Math Expert")
                .instructions(
                        "You are a personal math expert. When asked a question, write and run Python code to answer the question.")
                .tool(AssistantTool.CODE_INTERPRETER)
                .build();

        assistant = openAI.assistants().modify(assistant.getId(), assistantRequest).join();
        System.out.println(assistant);
    }

    public void demoListAssistants() {
        AtomicInteger count = new AtomicInteger();
        openAI.assistants()
                .getList()
                .join()
                .forEach(r -> System.out.println("\n#" + count.incrementAndGet() + "\n" + r));
    }

    public void demoUploadAssistantFile() {
        var fileRequest = FileRequest.builder()
                .file(Paths.get("src/demo/resources/code_interpreter_file.txt"))
                .purpose(PurposeType.ASSISTANTS)
                .build();
        var file = openAI.files().create(fileRequest).join();
        var assistantFile = openAI.assistants().createFile(assistantId, file.getId()).join();
        System.out.println(assistantFile);
        fileId = file.getId();
    }

    public void demoCreateThread() {
        var threadRequest = ThreadRequest.builder()
                .message(ThreadMessageRequest.builder()
                        .role("user")
                        .content(
                                "Inspect the content of the attached text file. After that plot graph of the formula requested in it.")
                        .build())
                .build();

        var thread = openAI.threads().create(threadRequest).join();
        System.out.println(thread);
        threadId = thread.getId();
    }

    public void demoRunThreadAndWaitUntilComplete() {
        var run = openAI.threads().createRun(threadId, assistantId).join();
        runId = run.getId();

        while (!run.getStatus().equals(ThreadRun.Status.COMPLETED)) {
            sleep(1);
            run = openAI.threads().getRun(run.getThreadId(), run.getId()).join();
        }
        System.out.println(run);

        var messages = openAI.threads().getMessageList(threadId).join();
        System.out.println(messages);
    }

    public void demoRunThreadAndStream() {
        var request = ThreadRunRequest.builder().assistantId(assistantId).build();
        var response = openAI.threads().createRunStream(threadId, request).join();
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
    }

    public void demoGetAssistantMessages() {
        List<ThreadMessage> messages = openAI.threads().getMessageList(threadId).join();
        ThreadMessage assistant = messages.get(0);
        ImageFileContent assistantImageContent = assistant.getContent()
                .stream()
                .filter(ImageFileContent.class::isInstance)
                .map(ImageFileContent.class::cast)
                .findFirst()
                .orElse(null);

        System.out.println("All messages:");
        System.out.println("=============");
        System.out.println(messages);

        if (assistantImageContent != null) {
            System.out.println("\nAssistant answer contains an image. Downloading it now...");
            try (var in = openAI.files()
                    .getContentInputStream(assistantImageContent.getImageFile().getFileId())
                    .join()) {
                Path tempFile = Files.createTempFile("code_interpreter", ".png");
                Files.write(tempFile, in.readAllBytes());
                System.out.println("Image file downloaded to: " + tempFile.toUri());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void demoDeleteAssistantAndThread() {
        openAI.assistants().delete(assistantId).join();
        openAI.threads().delete(threadId).join();
    }

    private static void sleep(int seconds) {
        try {
            java.lang.Thread.sleep(1000L * seconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        var demo = new AssistantServiceDemo();

        demo.addTitleAction("Demo Call Assistant Create", demo::demoCreateAssistant);
        demo.addTitleAction("Demo Call Assistant Retrieve and Modify", demo::demoRetrieveAndModifyAssistant);
        demo.addTitleAction("Demo Call Assistant List", demo::demoListAssistants);
        demo.addTitleAction("Demo Call Assistant File Upload", demo::demoUploadAssistantFile);
        demo.addTitleAction("Demo Call Assistant Thread Create", demo::demoCreateThread);
        //demo.addTitleAction("Demo Call Assistant Thread Run", demo::demoRunThreadAndWaitUntilComplete);
        demo.addTitleAction("Demo Call Assistant Thread Run Stream", demo::demoRunThreadAndStream);
        demo.addTitleAction("Demo Call Assistant Messages Get", demo::demoGetAssistantMessages);
        demo.addTitleAction("Demo Call Assistant & Thread Delete", demo::demoDeleteAssistantAndThread);

        demo.run();
    }

}
