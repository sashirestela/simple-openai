package io.github.sashirestela.openai.demo;

import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import io.github.sashirestela.openai.domain.OpenAIDeletedResponse;
import io.github.sashirestela.openai.domain.file.FileRequest;
import io.github.sashirestela.openai.domain.file.FileResponse;

public class FileServiceDemo extends AbstractDemo {

  private String fileId;

  public FileServiceDemo() {
  }

  public FileResponse createFileResponse() {
    FileRequest fileRequest = FileRequest.builder()
        .file(Paths.get("src/demo/resources/test_data.jsonl"))
        .purpose("fine-tune")
        .build();
    CompletableFuture<FileResponse> futureFile = openAI.files().create(fileRequest);
    FileResponse fileResponse = futureFile.join();
    return fileResponse;
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
    CompletableFuture<OpenAIDeletedResponse> futureFile = openAI.files().delete(fileId);
    OpenAIDeletedResponse fileDeleted = futureFile.join();
    return fileDeleted;
  }

  public void demoCallFileCreate() {
    FileResponse fileResponse = createFileResponse();
    fileId = fileResponse.getId();
    System.out.println(fileResponse);
  }

  public void demoCallFileGetList() {
    CompletableFuture<List<FileResponse>> futureFile = openAI.files().getList();
    List<FileResponse> fileResponses = futureFile.join();
    fileResponses.stream()
        .forEach(System.out::println);
  }

  public void demoCallFileGetOne() {
    CompletableFuture<FileResponse> futureFile = openAI.files().getOne(fileId);
    FileResponse fileResponse = futureFile.join();
    System.out.println(fileResponse);
  }

  public void demoCallFileGetContent() {
    CompletableFuture<String> futureFile = openAI.files().getContent(fileId);
    String fileContent = futureFile.join();
    System.out.println(fileContent);
  }

  public void demoCallFileDelete() {
    OpenAIDeletedResponse fileDeleted = deleteFileResponse(fileId);
    System.out.println(fileDeleted);
  }

  public static void main(String[] args) {
    FileServiceDemo demo = new FileServiceDemo();

    demo.addTitleAction("Call File Create", () -> demo.demoCallFileCreate());
    demo.addTitleAction("Call File List", () -> demo.demoCallFileGetList());
    demo.addTitleAction("Call File One", () -> demo.demoCallFileGetOne());
    demo.addTitleAction("Call File Content", () -> demo.demoCallFileGetContent());
    demo.addTitleAction("Call File Delete", () -> demo.demoCallFileDelete());

    demo.run();
  }
}