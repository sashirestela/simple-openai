package io.github.sashirestela.openai.demo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import io.github.sashirestela.openai.domain.PageRequest;
import io.github.sashirestela.openai.domain.Page;
import io.github.sashirestela.openai.domain.assistant.AssistantFunction;
import io.github.sashirestela.openai.domain.assistant.AssistantRequest;
import io.github.sashirestela.openai.domain.assistant.AssistantTool;
import io.github.sashirestela.openai.domain.assistant.ThreadCreateAndRunRequest;
import io.github.sashirestela.openai.domain.assistant.ThreadMessage;
import io.github.sashirestela.openai.domain.assistant.ThreadMessageList;
import io.github.sashirestela.openai.domain.assistant.ThreadMessageRequest;
import io.github.sashirestela.openai.domain.assistant.ThreadRequest;
import io.github.sashirestela.openai.domain.assistant.ThreadRun;
import io.github.sashirestela.openai.domain.assistant.ThreadRunRequest;
import io.github.sashirestela.openai.domain.assistant.ThreadRunStep;
import io.github.sashirestela.openai.domain.chat.ChatFunction;
import io.github.sashirestela.openai.domain.file.FileRequest;
import io.github.sashirestela.openai.function.FunctionExecutor;
import io.github.sashirestela.openai.function.Functional;

import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class AssistantServiceDemo extends AbstractDemo {

    String assistantId;
    String fileId;
    String threadId;

    public void demoCreateAssistant() {
        var assistantRequest = AssistantRequest.builder()
                .model("gpt-3.5-turbo")
                .build();

        var assistant = openAI.assistants().create(assistantRequest).join();
        System.out.println(assistant);
        assistantId = assistant.getId();
    }

    public void demoRetrieveAndModifyAssistant() {
        var assistant = openAI.assistants().getOne(assistantId).join();
        var assistantRequest = assistant.mutate()
                .name("Math Tutor")
                .instructions("You are a personal math tutor. When asked a question, write and run Python code to answer the question.")
                .tool(AssistantTool.CODE_INTERPRETER)
                .build();

        assistant = openAI.assistants().modify(assistant.getId(), assistantRequest).join();
        System.out.println(assistant);
    }

    public void demoListAssistants() {
        AtomicInteger count = new AtomicInteger();
        openAI.assistants().getList()
                .join()
                .forEach(r -> System.out.println("\n#"+count.incrementAndGet()+"\n" + r));
    }

    public void demoUploadAssistantFile() {
        var fileRequest = FileRequest.builder()
                .file(Paths.get("src/demo/resources/code_interpreter_file.txt"))
                .purpose("assistants")
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
                        .content("Inspect the content of the attached text file. After that plot graph of the formula requested in it.")
                        .build())
                .build();

        var thread = openAI.threads().create(threadRequest).join();
        System.out.println(thread);
        threadId = thread.getId();
    }

    public void demoRunThreadAndWaitUntilComplete() {
        var run = openAI.threads().createRun(threadId, assistantId).join();

        while (!run.getStatus().equals(ThreadRun.Status.COMPLETED)) {
            sleep(1);
            run = openAI.threads().getRun(run.getThreadId(), run.getId()).join();
        }
        System.out.println(run);

        var messages = openAI.threads().getMessageList(threadId).join();
        System.out.println(messages);
    }

    public void demoDeleteAssistant() {
        openAI.assistants().delete(assistantId).join();
    }

    public void demoCallAssistantsCreate2() {
        // Create assistant
        var assistantRequest = AssistantRequest.builder()
                .model("gpt-3.5-turbo")
                .build();
        var assistant = openAI.assistants().create(assistantRequest).join();

        // Create and run thread
        var runRequest = ThreadCreateAndRunRequest.builder()
                .assistantId(assistant.getId())
                .thread(ThreadMessageList.builder()
                        .message(ThreadMessageRequest.builder()
                                .role("user")
                                .content("Explain deep learning to a 5 year old.")
                                .build())
                        .build())
                .build();
        var run = openAI.threads().createThreadAndRun(runRequest).join();
        System.out.println(run);

        // Wait until run is completed
        while (!run.getStatus().equals(ThreadRun.Status.COMPLETED)) {
            sleep(1);
            run = openAI.threads().getRun(run.getThreadId(), run.getId()).join();
        }

        List<ThreadMessage> messages = openAI.threads().getMessageList(run.getThreadId()).join();
        System.out.println(messages);

        Page<ThreadRunStep> steps = openAI.threads().getRunStepList(run.getThreadId(), run.getId()).join();
        ThreadRunStep lastStep = openAI.threads().getRunStep(run.getThreadId(), run.getId(), steps.getLastId()).join();
        System.out.println(lastStep);

        // Clean up by deleting current assistant
        openAI.assistants().delete(assistant.getId()).join();
    }

    public void demoCallAssistantsCreate() {
        ChatFunction getWeatherFunction = ChatFunction.builder()
                .name("get_weather")
                .description("Get the current weather of a location")
                .functionalClass(ChatServiceDemo.Weather.class)
                .build();
        FunctionExecutor functionExecutor = new FunctionExecutor();
        functionExecutor.enrollFunction(getWeatherFunction);

        functionExecutor.getFunctions();
        var assistantRequest = AssistantRequest.builder()
                .model("gpt-3.5-turbo")
                .name("Math Tutor")
                .instructions("You are a personal math tutor. When asked a question, write and run Python code to answer the question.")
                .tool(AssistantTool.CODE_INTERPRETER)
                .tool(AssistantTool.builder()
                        .function(AssistantFunction.function(getWeatherFunction))
                        .build())
                .build();
        var assistant = openAI.assistants().create(assistantRequest)
                .join();
        assistant = openAI.assistants().getOne(assistant.getId())
                .join();
        var thread = openAI.threads().create()
                .join();
        openAI.threads().createMessage(thread.getId(), ThreadMessageRequest.builder()
                        .role("user")
                        .content("What's weather like in Sitka?")
                        .build())
                .join();
        ThreadRun run = openAI.threads().createRun(thread.getId(), ThreadRunRequest.builder()
                        .assistantId(assistant.getId())
                .build())
                .join();
        while (!run.getStatus().equals(ThreadRun.Status.REQUIRES_ACTION)) {
            sleep(1);
            run = openAI.threads().getRun(thread.getId(), run.getId()).join();
        }
        run = openAI.threads().modifyRun(run.getThreadId(), run.getId(), ThreadRunRequest.builder()
                        .metadata(Map.of("k", "v"))
                .build()).join();
        var runs = openAI.threads().getRunList(run.getThreadId(), PageRequest.builder().build()).join();

        run = openAI.threads().submitToolOutputs(run.getThreadId(), run.getId(), functionExecutor.executeAll(run.getRequiredToolCalls())).join();

        System.out.println(run);
        run = openAI.threads().cancelRun(run.getThreadId(), run.getId()).join();
//        while (!run.getStatus().equals(ThreadRun.Status.COMPLETED)) {
//            sleep(1);
//            run = openAI.threads().getRun(thread.getId(), run.getId()).join();
//        }
//        System.out.println(run);


        Page<ThreadMessage> messages = openAI.threads().getMessageList(thread.getId()).join();

        openAI.assistants().delete(assistant.getId()).join();
        System.out.println(assistant);
//        System.out.println(thread);
//        System.out.println(message);
        System.out.println(run);
        System.out.println(runs);
        System.out.println(messages);
    }

    public static class Weather implements Functional {
        @JsonPropertyDescription("City and state, for example: León, Guanajuato")
        public String location;

        @JsonPropertyDescription("The temperature unit, can be 'celsius' or 'fahrenheit'")
        @JsonProperty(required = true)
        public String unit;

        @Override
        public Object execute() {
            return Math.random() * 45;
        }
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
        demo.addTitleAction("Demo Call Assistant Thread Run", demo::demoRunThreadAndWaitUntilComplete);
        demo.addTitleAction("Demo Call Assistant Delete", demo::demoDeleteAssistant);

        demo.run();
    }
}