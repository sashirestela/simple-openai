package io.github.sashirestela.openai.demo;

import io.github.sashirestela.openai.domain.file.FileRequest.PurposeType;
import io.github.sashirestela.openai.domain.upload.UploadCompleteRequest;
import io.github.sashirestela.openai.domain.upload.UploadPartRequest;
import io.github.sashirestela.openai.domain.upload.UploadRequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class UploadDemo extends AbstractDemo {

    private String fileName;
    private String splitPath;
    private String mimeType;
    private long totalBytes;
    private int totalSplits;
    private String uploadId;
    private List<String> uploadPartIds;

    public void splitSourceFile() throws IOException {
        String fullFileName;
        String chunkSizeMB;

        while ((fullFileName = System.console().readLine("Enter the full file name to upload: ")).isBlank());
        while ((chunkSizeMB = System.console().readLine("Enter the chunk size in MB: ")).isBlank());
        while ((splitPath = System.console().readLine("Enter the path to put splittings: ")).isBlank());
        File sourceFile = new File(fullFileName);
        fileName = sourceFile.getName();
        mimeType = Files.probeContentType(Path.of(fullFileName));
        FileInputStream fis = new FileInputStream(sourceFile);
        int chunkSize = Integer.parseInt(chunkSizeMB) * 1024 * 1024;
        byte[] buffer = new byte[chunkSize];
        int bytesRead = 0;
        int chunkIndex = 1;
        while ((bytesRead = fis.read(buffer)) > 0) {
            File chunkFile = new File(splitPath, fileName + ".part" + chunkIndex);
            try (FileOutputStream fos = new FileOutputStream(chunkFile)) {
                fos.write(buffer, 0, bytesRead);
            }
            totalBytes += bytesRead;
            chunkIndex++;
        }
        fis.close();
        totalSplits = chunkIndex - 1;
    }

    public void demoCreateUpload() {
        var uploadRequest = UploadRequest.builder()
                .filename(fileName)
                .purpose(PurposeType.ASSISTANTS)
                .bytes(totalBytes)
                .mimeType(mimeType)
                .build();
        var uploadResponse = openAI.uploads().create(uploadRequest).join();
        uploadId = uploadResponse.getId();
        System.out.println(uploadResponse);
    }

    public void demoAddPartsUpload() {
        uploadPartIds = new ArrayList<>();
        for (int i = 1; i <= totalSplits; i++) {
            Path partPath = Path.of(splitPath, fileName + ".part" + i);
            var uploadPartResponse = openAI.uploads()
                    .addPart(uploadId,
                            UploadPartRequest.builder().data(partPath).build())
                    .join();
            uploadPartIds.add(uploadPartResponse.getId());
            System.out.println(uploadPartResponse);
        }
    }

    public void demoCompleteUpload() {
        var uploadCompleteResponse = openAI.uploads()
                .complete(uploadId,
                        UploadCompleteRequest.builder().partIds(uploadPartIds).build())
                .join();
        System.out.println(uploadCompleteResponse);

        var fileId = uploadCompleteResponse.getFile().getId();
        FileDemo fileServiceDemo = new FileDemo();
        fileServiceDemo.waitUntilFileIsProcessed(fileId);
        System.out.println("The file was processed.");
        fileServiceDemo.deleteFile(fileId);
        System.out.println("The file was deleted.");
    }

    public void demoCancelUpload() {
        var uploadRequest = UploadRequest.builder()
                .filename(fileName + ".tmp")
                .purpose(PurposeType.ASSISTANTS)
                .bytes(totalBytes)
                .mimeType(mimeType)
                .build();
        var uploadResponse = openAI.uploads().create(uploadRequest).join();
        var uploadIdToCancel = uploadResponse.getId();

        var uploadCancelResponse = openAI.uploads().cancel(uploadIdToCancel).join();
        System.out.println(uploadCancelResponse);
    }

    public static void main(String[] args) throws IOException {
        var demo = new UploadDemo();

        demo.splitSourceFile();

        demo.addTitleAction("Call Upload Create", demo::demoCreateUpload);
        demo.addTitleAction("Call Upload Add Parts", demo::demoAddPartsUpload);
        demo.addTitleAction("Call Upload Complete", demo::demoCompleteUpload);
        demo.addTitleAction("Call Upload Cancel", demo::demoCancelUpload);

        demo.run();
    }

}
