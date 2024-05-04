package io.github.sashirestela.openai.demo;

import io.github.sashirestela.openai.domain.batch.BatchRequest;
import io.github.sashirestela.openai.domain.batch.BatchRequest.CompletionWindowType;
import io.github.sashirestela.openai.domain.batch.EndpointType;
import io.github.sashirestela.openai.domain.file.FileRequest.PurposeType;

import java.util.Map;

public class BatchServiceDemo extends AbstractDemo {

    private FileServiceDemo fileServiceDemo;
    private String fileId;
    private String batchId;

    public BatchServiceDemo() {
        fileServiceDemo = new FileServiceDemo();
    }

    public void demoCallBatchCreate() {
        System.out.println("Uploading a file ...");
        var fileResponse = fileServiceDemo.createFile("src/demo/resources/batch_request.jsonl", PurposeType.BATCH);
        fileId = fileResponse.getId();
        System.out.println("Waiting until its status is 'processed' ...");
        fileServiceDemo.waitUntilFileIsProcessed(fileId);
        var batchRequest = BatchRequest.builder()
                .inputFileId(fileId)
                .endpoint(EndpointType.CHAT_COMPLETIONS)
                .completionWindow(CompletionWindowType.T24H)
                .metadata(Map.of("key1", "value1"))
                .build();
        var batchResponse = openAI.batches().create(batchRequest).join();
        batchId = batchResponse.getId();
        System.out.println(batchResponse);
    }

    public void demoCallBatchGetOne() {
        var batchResponse = openAI.batches().getOne(batchId).join();
        System.out.println(batchResponse);
    }

    public void demoCallBatchGetList() {
        var batchResponse = openAI.batches().getList(null, null).join();
        batchResponse.stream()
                .forEach(System.out::println);
    }

    public void demoCallBatchCancel() {
        var batchResponse = openAI.batches().cancel(batchId).join();
        System.out.println(batchResponse);
        fileServiceDemo.deleteFile(fileId);
    }

    public static void main(String[] args) {
        var demo = new BatchServiceDemo();

        demo.addTitleAction("Call Batch Create", demo::demoCallBatchCreate);
        demo.addTitleAction("Call Batch One", demo::demoCallBatchGetOne);
        demo.addTitleAction("Call Batch List", demo::demoCallBatchGetList);
        demo.addTitleAction("Call Batch Cancel", demo::demoCallBatchCancel);

        demo.run();
    }

}
