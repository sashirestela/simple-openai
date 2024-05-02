package io.github.sashirestela.openai.demo;

import io.github.sashirestela.openai.domain.assistant.v2.Attachment;
import io.github.sashirestela.openai.domain.assistant.v2.ThreadMessageRequest;
import io.github.sashirestela.openai.domain.assistant.v2.ThreadModifyRequest;
import io.github.sashirestela.openai.domain.assistant.v2.ThreadRequest;
import io.github.sashirestela.openai.domain.assistant.v2.Attachment.AttachmentTool;
import io.github.sashirestela.openai.domain.assistant.v2.ThreadMessageRequest.ThreadMessageRole;
import io.github.sashirestela.openai.domain.file.PurposeType;

import java.util.Map;

public class ThreadV2Demo extends AbstractDemo {

    private FileServiceDemo fileDemo;
    private String fileId;
    private String threadId;

    public ThreadV2Demo() {
        fileDemo = new FileServiceDemo();
        var file = fileDemo.createFile("src/demo/resources/mistral-ai.txt",
                PurposeType.ASSISTANTS);
        fileId = file.getId();
    }

    public void createThread() {
        var threadRequest = ThreadRequest.builder()
                .message(ThreadMessageRequest.builder()
                        .role(ThreadMessageRole.ASSISTANT)
                        .content("Hi, how can I help ypu?")
                        .build())
                .message(ThreadMessageRequest.builder()
                        .role(ThreadMessageRole.USER)
                        .content("Please, tell me what an LLM is?")
                        .attachment(Attachment.builder()
                                .fileId(fileId)
                                .tool(AttachmentTool.FILE_SEARCH)
                                .build())
                        .metadata(Map.of("item", "first"))
                        .build())
                .metadata(Map.of("env", "test"))
                .build();
        var thread = openAI.threads().create(threadRequest).join();
        System.out.println(thread);
        threadId = thread.getId();
    }

    public void modifyThread() {
        var threadModifyRequest = ThreadModifyRequest.builder()
                .metadata(Map.of("env", "testing"))
                .metadata(Map.of("user", "thomas"))
                .build();
        var thread = openAI.threads().modify(threadId, threadModifyRequest).join();
        System.out.println(thread);
    }

    public void retrieveThread() {
        var thread = openAI.threads().getOne(threadId).join();
        System.out.println(thread);
    }

    public void deleteThread() {
        var thread = openAI.threads().getOne(threadId).join();
        var vectorStoreId = thread.getToolResources().getFileSearch().getVectorStoreIds().get(0);

        var deletedFile = fileDemo.deleteFile(fileId);
        System.out.println(deletedFile);

        var deletedVectorStore = openAI.vectorStores().delete(vectorStoreId).join();
        System.out.println(deletedVectorStore);

        var deletedThread = openAI.threads().delete(threadId).join();
        System.out.println(deletedThread);
    }

    public static void main(String[] args) {
        var demo = new ThreadV2Demo();
        demo.addTitleAction("Demo Thread v2 Create", demo::createThread);
        demo.addTitleAction("Demo Thread v2 Modify", demo::modifyThread);
        demo.addTitleAction("Demo Thread v2 Retrieve", demo::retrieveThread);
        demo.addTitleAction("Demo Thread v2 Delete", demo::deleteThread);
        demo.run();
    }

}
