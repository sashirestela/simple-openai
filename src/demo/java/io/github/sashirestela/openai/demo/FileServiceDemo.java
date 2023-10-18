package io.github.sashirestela.openai.demo;

import java.nio.file.Paths;

import io.github.sashirestela.openai.domain.OpenAIDeletedResponse;
import io.github.sashirestela.openai.domain.file.FileRequest;
import io.github.sashirestela.openai.domain.file.FileResponse;

public class FileServiceDemo extends AbstractDemo {

  private String fileId;

  public FileResponse createFileResponse() {
    var fileRequest = FileRequest.builder()
        .file(Paths.get("src/demo/resources/test_data.jsonl"))
        .purpose("fine-tune")
        .build();
    var futureFile = openAI.files().create(fileRequest);
    return futureFile.join();
  }

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

  public OpenAIDeletedResponse deleteFileResponse(String fileId) {
    waitUntilFileIsProcessed(fileId);
    var futureFile = openAI.files().delete(fileId);
    return futureFile.join();
  }

  public void demoCallFileCreate() {
    var fileResponse = createFileResponse();
    fileId = fileResponse.getId();
    System.out.println(fileResponse);
  }

  public void demoCallFileGetList() {
    var futureFile = openAI.files().getList();
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
    var fileDeleted = deleteFileResponse(fileId);
    System.out.println(fileDeleted);
  }

  public static void main(String[] args) {
    var demo = new FileServiceDemo();

    demo.addTitleAction("Call File Create", demo::demoCallFileCreate);
    demo.addTitleAction("Call File List", demo::demoCallFileGetList);
    demo.addTitleAction("Call File One", demo::demoCallFileGetOne);
    demo.addTitleAction("Call File Content", demo::demoCallFileGetContent);
    demo.addTitleAction("Call File Delete", demo::demoCallFileDelete);

    demo.run();
  }
}