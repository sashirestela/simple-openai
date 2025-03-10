package io.github.sashirestela.openai.demo;

import io.github.sashirestela.openai.common.content.ContentPart.ContentPartTextAnnotation;
import io.github.sashirestela.openai.common.function.FunctionDef;
import io.github.sashirestela.openai.common.function.FunctionExecutor;
import io.github.sashirestela.openai.demo.ThreadRunV2Demo.CurrentTemperature;
import io.github.sashirestela.openai.demo.ThreadRunV2Demo.RainProbability;
import io.github.sashirestela.openai.domain.assistant.AssistantRequest;
import io.github.sashirestela.openai.domain.assistant.ThreadMessageRequest;
import io.github.sashirestela.openai.domain.assistant.ThreadMessageRole;
import io.github.sashirestela.openai.domain.assistant.ThreadRunRequest;
import io.github.sashirestela.openai.service.AssistantServices;

import java.util.ArrayList;
import java.util.List;

public class ThreadRunStepV2Demo extends AbstractDemo {

    private String assistantId;
    private String threadId;
    private String threadRunId;
    private String threadRunStepId;

    protected String model;
    protected AssistantServices assistantProvider;

    protected ThreadRunStepV2Demo(String model) {
        this("standard", model);
    }

    protected ThreadRunStepV2Demo(String provider, String model) {
        super(provider);
        this.model = model;
        this.assistantProvider = this.openAI;
    }

    public void prepareDemo() {
        FunctionExecutor functionExecutor;
        List<FunctionDef> functionList = new ArrayList<>();
        functionList.add(FunctionDef.builder()
                .name("CurrentTemperature")
                .description("Get the current temperature for a specific location")
                .functionalClass(CurrentTemperature.class)
                .build());
        functionList.add(FunctionDef.builder()
                .name("RainProbability")
                .description("Get the probability of rain for a specific location")
                .functionalClass(RainProbability.class)
                .build());

        functionExecutor = new FunctionExecutor();
        functionExecutor.enrollFunctions(functionList);

        var assistant = assistantProvider.assistants()
                .create(AssistantRequest.builder()
                        .model(this.model)
                        .name("Demo Assistant")
                        .instructions("You are a very kind assistant. If you cannot find correct facts to answer the "
                                + "questions, you have to refer to the attached files or use the functions provided. "
                                + "Finally, if you receive math questions, you must write and run code to answer them.")
                        .tools(functionExecutor.getToolFunctions())
                        .temperature(0.2)
                        .build())
                .join();
        assistantId = assistant.getId();
    }

    public void createThreadRun() {
        var thread = assistantProvider.threads().create().join();
        threadId = thread.getId();

        var question = "Tell me something brief about Lima Peru, then tell me how's "
                + "the weather and rain probability there right now.";
        System.out.println("Question: " + question);
        var threadRunRequest = ThreadRunRequest.builder()
                .assistantId(assistantId)
                .additionalMessage(ThreadMessageRequest.builder()
                        .role(ThreadMessageRole.USER)
                        .content(question)
                        .build())
                .build();
        var threadRun = assistantProvider.threadRuns().createAndPoll(threadId, threadRunRequest);
        var threadMessages = assistantProvider.threadMessages().getList(threadId).join();
        var answer = ((ContentPartTextAnnotation) threadMessages.first().getContent().get(0)).getText().getValue();
        System.out.println("Answer: " + answer);
        threadRunId = threadRun.getId();
    }

    public void listThreadRunSteps() {
        var threadRunSteps = assistantProvider.threadRunSteps().getList(threadId, threadRunId).join();
        threadRunSteps.forEach(System.out::println);
        threadRunStepId = threadRunSteps.first().getId();
    }

    public void retrieveThreadRunStep() {
        var threadRunStep = assistantProvider.threadRunSteps().getOne(threadId, threadRunId, threadRunStepId).join();
        System.out.println(threadRunStep);
    }

    public void retrieveThreadRunStepWithFilters() {
        var threadRunStep = assistantProvider.threadRunSteps()
                .getOneWithFileSearchResult(threadId, threadRunId, threadRunStepId)
                .join();
        System.out.println(threadRunStep);
    }

    public void deleteDemo() {
        var deletedThread = assistantProvider.threads().delete(threadId).join();
        System.out.println(deletedThread);

        var deletedAssistant = assistantProvider.assistants().delete(assistantId).join();
        System.out.println(deletedAssistant);
    }

    public static void main(String[] args) {
        var demo = new ThreadRunStepV2Demo("gpt-4o-mini");
        demo.prepareDemo();
        demo.addTitleAction("Demo ThreadRun v2 Create", demo::createThreadRun);
        demo.addTitleAction("Demo ThreadRunStep v2 List", demo::listThreadRunSteps);
        demo.addTitleAction("Demo ThreadRunStep v2 Retrieve", demo::retrieveThreadRunStep);
        demo.addTitleAction("Demo ThreadRunStep v2 Retrieve with Filters", demo::retrieveThreadRunStepWithFilters);
        demo.addTitleAction("Demo ThreadRun v2 Delete", demo::deleteDemo);
        demo.run();
    }

}
