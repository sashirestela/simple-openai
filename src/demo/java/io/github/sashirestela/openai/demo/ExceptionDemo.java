package io.github.sashirestela.openai.demo;

import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.exception.OpenAIException.AuthenticationException;
import io.github.sashirestela.openai.exception.OpenAIException.NotFoundException;
import io.github.sashirestela.openai.exception.OpenAIExceptionConverter;

public class ExceptionDemo {

    public static void demoAuthenticationException() {
        var openAI = SimpleOpenAI.builder()
                .apiKey("bad-apikey")
                .build();
        var models = openAI.models().getList().join();
        models.forEach(System.out::println);
    }

    public static void demoNotFoundException() {
        var openAI = SimpleOpenAI.builder()
                .apiKey(System.getenv("OPENAI_API_KEY"))
                .build();
        var model = openAI.models().getOne("bad-model").join();
        System.out.println(model);
    }

    public static void demoGeneralException() {
        var openAI = SimpleOpenAI.builder()
                .apiKey(System.getenv("OPENAI_API_KEY"))
                .baseUrl("https://bad.url")
                .build();
        var models = openAI.models().getList().join();
        models.forEach(System.out::println);
    }

    public static void main(String[] args) {
        for (int i = 1; i <= 3; i++) {
            try {
                System.out.println("=".repeat(80));
                if (i == 1) {
                    demoAuthenticationException();
                }
                if (i == 2) {
                    demoNotFoundException();
                }
                if (i == 3) {
                    demoGeneralException();
                }
            } catch (Exception e) {
                try {
                    OpenAIExceptionConverter.rethrow(e);
                } catch (AuthenticationException ae) {
                    var info = ae.getResponseInfo();
                    System.out.println("Http Response Status: " + info.getStatus());
                    System.out.println("OpenAI Error Message: " + info.getErrorResponse().getError().getMessage());
                } catch (NotFoundException ne) {
                    var info = ne.getResponseInfo();
                    System.out.println("Http Response Status: " + info.getStatus());
                    System.out.println("OpenAI Error Message: " + info.getErrorResponse().getError().getMessage());
                } catch (RuntimeException re) {
                    System.out.println("Error Message: " + re.getMessage());
                }
            }
        }
        System.out.println("=".repeat(80));
    }

}
