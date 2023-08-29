package io.github.sashirestela.openai.http;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.github.sashirestela.openai.SimpleUncheckedException;
import io.github.sashirestela.openai.support.Constant;

public class HttpMultipart {
  private static HttpMultipart multipart = null;

  private final static String DASH = "--";
  private final static String DQ = "\"";
  private final static String NL = "\r\n";
  private final static String DISPOSITION = "Content-Disposition: form-data";
  private final static String FIELD_NAME = "; name=";
  private final static String FILE_NAME = "; filename=";
  private final static String CONTENT_TYPE = "Content-Type: ";

  private HttpMultipart() {
  }

  public static HttpMultipart get() {
    if (multipart == null) {
      multipart = new HttpMultipart();
    }
    return multipart;
  }

  public List<byte[]> toByteArrays(Map<String, Object> data) {
    List<byte[]> byteArrays = new ArrayList<>();
    for (Map.Entry<String, Object> entry : data.entrySet()) {
      byteArrays.add(toBytes(DASH + Constant.BOUNDARY_VALUE + NL));
      byteArrays.add(toBytes(DISPOSITION));
      String fieldName = entry.getKey();
      if (entry.getValue() instanceof Path) {
        String fileName = null;
        String mimeType = null;
        byte[] fileContent = null;
        try {
          Path path = (Path) entry.getValue();
          fileName = path.toString();
          mimeType = Files.probeContentType(path);
          fileContent = Files.readAllBytes(path);
        } catch (IOException e) {
          throw new SimpleUncheckedException("Error trying to read the file {0}.", fileName, e);
        }
        byteArrays.add(toBytes(FIELD_NAME + DQ + fieldName + DQ + FILE_NAME + DQ + fileName + DQ + NL));
        byteArrays.add(toBytes(CONTENT_TYPE + mimeType + NL));
        byteArrays.add(toBytes(NL));
        byteArrays.add(fileContent);
        byteArrays.add(toBytes(NL));
      } else {
        Object fieldValue = entry.getValue();
        byteArrays.add(toBytes(FIELD_NAME + DQ + fieldName + DQ + NL));
        byteArrays.add(toBytes(NL));
        byteArrays.add(toBytes(fieldValue + NL));
      }
    }
    byteArrays.add(toBytes(DASH + Constant.BOUNDARY_VALUE + DASH + NL));
    return byteArrays;
  }

  private byte[] toBytes(String text) {
    return text.getBytes(StandardCharsets.UTF_8);
  }
}