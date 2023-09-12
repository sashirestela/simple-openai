package io.github.sashirestela.openai.function;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.github.sashirestela.openai.support.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class ParametersSerializerTest {

  @Test
  void shouldApplyThisSerializerWhenAFieldIsAnnotatedWithTheSerializer() {
    var sample = new SampleClass(1, Serialized.class);
    var actualJson = JsonUtil.get().objectToJson(sample);
    var expectedJson = "{\"id\":1,\"serialized\":{\"type\":\"object\",\"properties\":" +
        "{\"first\":{\"type\":\"string\"},\"second\":{\"type\":\"integer\"}},\"required\":[\"first\"]}}";
    assertEquals(expectedJson, actualJson);
  }

  @AllArgsConstructor
  @Getter
  static class SampleClass {

    private Integer id;

    @JsonSerialize(using = ParametersSerializer.class)
    private Class<?> serialized;

  }

  static class Serialized {

    @JsonProperty(required = true)
    public String first;

    public Integer second;

  }
}