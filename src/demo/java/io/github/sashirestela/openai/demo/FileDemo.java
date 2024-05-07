package io.github.sashirestela.openai.demo;

import io.github.sashirestela.openai.common.DeletedObject;
import io.github.sashirestela.openai.domain.file.FileRequest;
import io.github.sashirestela.openai.domain.file.FileRequest.PurposeType;
import io.github.sashirestela.openai.domain.file.FileResponse;

import java.nio.file.Paths;

public class FileDemo extends AbstractDemo {

    private String fileId;

    public FileResponse createFile(String filePath, PurposeType purpose) {
        var fileRequest = FileRequest.builder()
                .file(Paths.get(filePath))
                .purpose(purpose)
                .build();
        var futureFile = openAI.files().create(fileRequest);
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
            fileResponse = openAI.files().getOne(fileId).join();
        } while (!fileResponse.getStatus().equals("processed"));
    }

    public DeletedObject deleteFile(String fileId) {
        var futureFile = openAI.files().delete(fileId);
        return futureFile.join();
    }

    public void demoCallFileCreate() {
        var fileResponse = createFile("src/demo/resources/test_data.jsonl", PurposeType.FINE_TUNE);
        fileId = fileResponse.getId();
        System.out.println(fileResponse);
    }

    public void demoCallFileGetList() {
        var futureFile = openAI.files().getList(null);
        var fileResponses = futureFile.join();
        fileResponses.stream()
                .forEach(System.out::println);
    }

    public void demoCallFileGetOne() {
        var futureFile = openAI.files().getOne(fileId);
        var fileResponse = futureFile.join();
        System.out.println(fileResponse);
    }

    public void demoCallFileGetContent() {
        var futureFile = openAI.files().getContent(fileId);
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
        demo.addTitleAction("Call File Content", demo::demoCallFileGetContent);
        demo.addTitleAction("Call File Delete", demo::demoCallFileDelete);

        demo.run();
    }

}
