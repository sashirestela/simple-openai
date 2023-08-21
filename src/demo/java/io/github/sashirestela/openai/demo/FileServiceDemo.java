package io.github.sashirestela.openai.demo;

import java.io.File;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import io.github.sashirestela.openai.domain.file.FileDeletedResponse;
import io.github.sashirestela.openai.domain.file.FileRequest;
import io.github.sashirestela.openai.domain.file.FileResponse;

public class FileServiceDemo extends AbstractDemo {

  private String fileId;

  public FileServiceDemo() {
  }

  public void demoCallFileCreate() {
    FileRequest fileRequest = FileRequest.builder()
        .file(new File("src/demo/resources/test_data.jsonl"))
        .purpose("fine-tune")
        .build();
    CompletableFuture<FileResponse> futureFile = openAI.files().create(fileRequest);
    FileResponse fileResponse = futureFile.join();
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

  public void demoCallFileDelete() {
    FileResponse fileResponse = null;
    do {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
      fileResponse = openAI.files().getOne(fileId).join();
    } while (!fileResponse.getStatus().equals("processed"));
    CompletableFuture<FileDeletedResponse> futureFile = openAI.files().delete(fileId);
    FileDeletedResponse fileDeleted = futureFile.join();
    System.out.println(fileDeleted);
  }

  public static void main(String[] args) {
    FileServiceDemo demo = new FileServiceDemo();

    demo.addTitleAction("Call File Create", () -> demo.demoCallFileCreate());
    demo.addTitleAction("Call File List", () -> demo.demoCallFileGetList());
    demo.addTitleAction("Call File One", () -> demo.demoCallFileGetOne());
    demo.addTitleAction("Call File Delete", () -> demo.demoCallFileDelete());

    demo.run();
  }
}
