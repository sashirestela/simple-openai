package io.github.sashirestela.openai.demo;

import io.github.sashirestela.openai.common.content.ContentPart.ContentPartImageFile;
import io.github.sashirestela.openai.common.content.ContentPart.ContentPartImageFile.ImageFile;
import io.github.sashirestela.openai.common.content.ContentPart.ContentPartImageUrl;
import io.github.sashirestela.openai.common.content.ContentPart.ContentPartImageUrl.ImageUrl;
import io.github.sashirestela.openai.common.content.ContentPart.ContentPartText;
import io.github.sashirestela.openai.common.content.ContentPart.ContentPartTextAnnotation;
import io.github.sashirestela.openai.common.content.ImageDetail;
import io.github.sashirestela.openai.domain.assistant.AssistantRequest;
import io.github.sashirestela.openai.domain.assistant.Attachment;
import io.github.sashirestela.openai.domain.assistant.Attachment.AttachmentTool;
import io.github.sashirestela.openai.domain.assistant.ThreadMessageDelta;
import io.github.sashirestela.openai.domain.assistant.ThreadMessageModifyRequest;
import io.github.sashirestela.openai.domain.assistant.ThreadMessageRequest;
import io.github.sashirestela.openai.domain.assistant.ThreadMessageRole;
import io.github.sashirestela.openai.domain.assistant.ThreadRunRequest;
import io.github.sashirestela.openai.domain.assistant.events.EventName;
import io.github.sashirestela.openai.domain.file.FileRequest.PurposeType;
import io.github.sashirestela.openai.service.AssistantServices;

import java.util.List;
import java.util.Map;

public class ThreadMessageV2Demo extends AbstractDemo {

    protected FileDemo fileDemo;
    protected String fileId;
    protected String threadId;
    protected String threadMessageId;

    protected String model;
    protected AssistantServices assistantProvider;

    protected ThreadMessageV2Demo(String model) {
        this("standard", model, new FileDemo());
    }

    protected ThreadMessageV2Demo(String provider, String model, FileDemo fileDemo) {
        super(provider);
        this.model = model;
        this.assistantProvider = this.openAI;
        this.fileDemo = fileDemo;
    }

    public void prepareDemo() {
        var file = fileDemo.createFile("src/demo/resources/mistral-ai.txt", PurposeType.ASSISTANTS);
        fileId = file.getId();

        var thread = assistantProvider.threads().create().join();
        threadId = thread.getId();
    }

    public void createThreadMessage() {
        var threadMessageRequest = ThreadMessageRequest.builder()
                .role(ThreadMessageRole.USER)
                .content("Please, tell me what an LLM is?")
                .attachment(Attachment.builder()
                        .fileId(fileId)
                        .tool(AttachmentTool.FILE_SEARCH)
                        .build())
                .metadata(Map.of("item", "first"))
                .build();
        var threadMessage = assistantProvider.threadMessages().create(threadId, threadMessageRequest).join();
        System.out.println(threadMessage);
        threadMessageId = threadMessage.getId();
    }

    public void modifyThreadMessage() {
        var threadMessageModifyRequest = ThreadMessageModifyRequest.builder()
                .metadata(Map.of("item", "firstly", "user", "dummy"))
                .build();
        var threadMessage = assistantProvider.threadMessages()
                .modify(threadId, threadMessageId, threadMessageModifyRequest)
                .join();
        System.out.println(threadMessage);
    }

    public void retrieveThreadMessage() {
        var threadMessage = assistantProvider.threadMessages().getOne(threadId, threadMessageId).join();
        System.out.println(threadMessage);
    }

    public void listThreadMessages() {
        var threadMessages = assistantProvider.threadMessages().getList(threadId).join();
        threadMessages.forEach(System.out::println);
    }

    public void visionThreadMessage() {
        var question = "Do you see any similarity or difference between the attached images?";
        System.out.println("Question: " + question);
        var file = fileDemo.createFile("src/demo/resources/machupicchu.jpg", PurposeType.VISION);
        var assistant = assistantProvider.assistants()
                .create(AssistantRequest.builder()
                        .model(this.model)
                        .instructions("You are a tutor on geography.")
                        .build())
                .join();
        var newThread = assistantProvider.threads().create().join();
        assistantProvider.threadMessages()
                .create(newThread.getId(), ThreadMessageRequest.builder()
                        .role(ThreadMessageRole.USER)
                        .content(List.of(
                                ContentPartText.of(question),
                                ContentPartImageFile.of(ImageFile.of(file.getId(), ImageDetail.LOW)),
                                ContentPartImageUrl.of(ImageUrl.of(
                                        "https://upload.wikimedia.org/wikipedia/commons/e/eb/Machu_Picchu%2C_Peru.jpg",
                                        ImageDetail.LOW))))
                        .build())
                .join();
        var responseStream = assistantProvider.threadRuns()
                .createStream(newThread.getId(), ThreadRunRequest.builder()
                        .assistantId(assistant.getId())
                        .build())
                .join();
        System.out.print("Answer: ");
        responseStream.forEach(e -> {
            switch (e.getName()) {
                case EventName.THREAD_MESSAGE_DELTA:
                    var messageDeltaFirstContent = ((ThreadMessageDelta) e.getData()).getDelta().getContent().get(0);
                    if (messageDeltaFirstContent instanceof ContentPartTextAnnotation) {
                        System.out.print(((ContentPartTextAnnotation) messageDeltaFirstContent).getText().getValue());
                    }
                    break;
                default:
                    break;
            }
        });
        System.out.println();
        fileDemo.deleteFile(file.getId());
        assistantProvider.assistants().delete(assistant.getId()).join();
        assistantProvider.threads().delete(newThread.getId()).join();
    }

    public void deleteThreadMessage() {
        var thread = assistantProvider.threads().getOne(threadId).join();
        var vectorStoreId = thread.getToolResources().getFileSearch().getVectorStoreIds().get(0);

        var deletedFile = fileDemo.deleteFile(fileId);
        System.out.println(deletedFile);

        var deletedVectorStore = assistantProvider.vectorStores().delete(vectorStoreId).join();
        System.out.println(deletedVectorStore);

        var deletedThreadMessage = assistantProvider.threadMessages().delete(threadId, threadMessageId).join();
        System.out.println(deletedThreadMessage);

        var deletedThread = assistantProvider.threads().delete(threadId).join();
        System.out.println(deletedThread);

    }

    public static void main(String[] args) {
        var demo = new ThreadMessageV2Demo("gpt-4o-mini");
        demo.prepareDemo();
        demo.addTitleAction("Demo Thread Message v2 Create", demo::createThreadMessage);
        demo.addTitleAction("Demo Thread Message v2 Modify", demo::modifyThreadMessage);
        demo.addTitleAction("Demo Thread Message v2 Retrieve", demo::retrieveThreadMessage);
        demo.addTitleAction("Demo Thread Message v2 List", demo::listThreadMessages);
        demo.addTitleAction("Demo Thread Message v2 Vision", demo::visionThreadMessage);
        demo.addTitleAction("Demo Thread Message v2 Delete", demo::deleteThreadMessage);
        demo.run();
    }

}
