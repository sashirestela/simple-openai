package io.github.sashirestela.openai.demo;

import io.github.sashirestela.openai.common.content.ContentPart.ContentPartImageFile;
import io.github.sashirestela.openai.common.content.ContentPart.ContentPartImageFile.ImageFile;
import io.github.sashirestela.openai.common.content.ContentPart.ContentPartText;
import io.github.sashirestela.openai.common.content.ContentPart.ContentPartTextAnnotation;
import io.github.sashirestela.openai.common.content.ImageDetail;
import io.github.sashirestela.openai.domain.assistant.AssistantRequest;
import io.github.sashirestela.openai.domain.assistant.ThreadMessageDelta;
import io.github.sashirestela.openai.domain.assistant.ThreadMessageRequest;
import io.github.sashirestela.openai.domain.assistant.ThreadMessageRole;
import io.github.sashirestela.openai.domain.assistant.ThreadRunRequest;
import io.github.sashirestela.openai.domain.assistant.events.EventName;
import io.github.sashirestela.openai.domain.file.FileRequest.PurposeType;

import java.util.List;

public class ThreadMessageV2AzureDemo extends ThreadMessageV2Demo {

    protected ThreadMessageV2AzureDemo(String model) {
        super("azure", model, new FileAzureDemo());
        this.assistantProvider = this.openAIAzure;
    }

    @Override
    public void visionThreadMessage() {
        var question = "What do you see in the image?";
        System.out.println("Question: " + question);
        var file = fileDemo.createFile("src/demo/resources/machupicchu.jpg", PurposeType.ASSISTANTS);
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
                                ContentPartImageFile.of(ImageFile.of(file.getId(), ImageDetail.LOW))))
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

    public static void main(String[] args) {
        var demo = new ThreadMessageV2AzureDemo("N/A");
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
