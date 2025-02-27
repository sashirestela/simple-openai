package io.github.sashirestela.openai.demo;

import io.github.sashirestela.openai.common.DeletedObject;
import io.github.sashirestela.openai.domain.file.FileRequest;
import io.github.sashirestela.openai.domain.file.FileRequest.PurposeType;
import io.github.sashirestela.openai.domain.file.FileResponse;
import io.github.sashirestela.openai.service.FileServices;

import java.nio.file.Paths;

public class FileDemo extends AbstractDemo {

    private String fileId;

    protected FileServices fileProvider;

    protected FileDemo() {
        this("standard");
    }

    protected FileDemo(String provider) {
        super(provider);
        this.fileProvider = this.openAI;
    }

    public FileResponse createFile(String filePath, PurposeType purpose) {
        var fileRequest = FileRequest.builder()
                .file(Paths.get(filePath))
                .purpose(purpose)
                .build();
        var futureFile = fileProvider.files().create(fileRequest);
        return futureFile.join();
    }

    @SuppressWarnings("deprecation")
    public void waitUntilFileIsProcessed(String fileId) {
        FileResponse fileResponse = null;
        do {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            fileResponse = fileProvider.files().getOne(fileId).join();
        } while (!fileResponse.getStatus().equals("processed"));
    }

    public DeletedObject deleteFile(String fileId) {
        var futureFile = fileProvider.files().delete(fileId);
        return futureFile.join();
    }

    public void demoCallFileCreate() {
        var fileResponse = createFile("src/demo/resources/mistral-ai.txt", PurposeType.ASSISTANTS);
        fileId = fileResponse.getId();
        System.out.println(fileResponse);
    }

    public void demoCallFileGetList() {
        var futureFile = fileProvider.files().getList(null);
        var fileResponses = futureFile.join();
        fileResponses.stream()
                .forEach(System.out::println);
    }

    public void demoCallFileGetOne() {
        var futureFile = fileProvider.files().getOne(fileId);
        var fileResponse = futureFile.join();
        System.out.println(fileResponse);
    }

    public void demoCallFileGetContent() {
        var futureFile = fileProvider.files().getContent(fileId);
        var fileContent = futureFile.join();
        System.out.println(fileContent);
    }

    public void demoCallFileDelete() {
        waitUntilFileIsProcessed(fileId);
        var fileDeleted = deleteFile(fileId);
        System.out.println(fileDeleted);
    }

    public static void main(String[] args) {
        var demo = new FileDemo();

        demo.addTitleAction("Call File Create", demo::demoCallFileCreate);
        demo.addTitleAction("Call File List", demo::demoCallFileGetList);
        demo.addTitleAction("Call File One", demo::demoCallFileGetOne);
        demo.addTitleAction("Call File Delete", demo::demoCallFileDelete);

        demo.run();
    }

}
