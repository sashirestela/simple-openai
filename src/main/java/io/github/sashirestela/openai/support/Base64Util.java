package io.github.sashirestela.openai.support;

import io.github.sashirestela.openai.exception.SimpleOpenAIException;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public class Base64Util {

    private Base64Util() {
    }

    public static String encode(String filePath, MediaType mediaType) {
        try {
            Path path = Paths.get(filePath);
            byte[] bytes = Files.readAllBytes(path);
            String base64String = Base64.getEncoder().encodeToString(bytes);
            if (mediaType == null) {
                return base64String;
            } else {
                var extension = filePath.substring(filePath.lastIndexOf('.') + 1);
                var prefix = "data:" + value(mediaType) + "/" + extension + ";base64,";
                return prefix + base64String;
            }
        } catch (Exception e) {
            throw new SimpleOpenAIException("Cannot encode from file {0}.", filePath, e);
        }
    }

    public static void decode(String base64String, String filePath) {
        try {
            byte[] bytes = Base64.getDecoder().decode(base64String);
            File outputFile = new File(filePath);
            Files.write(outputFile.toPath(), bytes);
        } catch (Exception e) {
            throw new SimpleOpenAIException("Cannot decode to file {0}.", filePath, e);
        }
    }

    private static String value(MediaType mediaType) {
        return mediaType.name().toLowerCase();
    }

    public enum MediaType {
        IMAGE,
        AUDIO;
    }

}
