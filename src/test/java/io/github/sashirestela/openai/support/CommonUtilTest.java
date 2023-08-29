package io.github.sashirestela.openai.support;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

public class CommonUtilTest {

  @Test
  void shouldReturnTrueWhenListIsNullOrEmpty() {
    List<?>[] testData = { null, new ArrayList<>() };
    for (List<?> data : testData) {
      boolean actualCondition = CommonUtil.get().isNullOrEmpty(data);
      boolean expectedCondition = true;
      assertEquals(expectedCondition, actualCondition);
    }
  }

  @Test
  void shouldReturnFalseWhenListIsNotEmpty() {
    boolean actualCondition = CommonUtil.get().isNullOrEmpty(Arrays.asList("one", "two"));
    boolean expectedCondition = false;
    assertEquals(expectedCondition, actualCondition);
  }

  @Test
  void shouldReturnTrueWhenArrayIsNullOrEmpty() {
    Object[][] testData = { null, new String[] {} };
    for (Object[] data : testData) {
      boolean actualCondition = CommonUtil.get().isNullOrEmpty(data);
      boolean expectedCondition = true;
      assertEquals(expectedCondition, actualCondition);
    }
  }

  @Test
  void shouldReturnFalseWhenArrayIsNotEmpty() {
    boolean actualCondition = CommonUtil.get().isNullOrEmpty(new String[] { "one", "two" });
    boolean expectedCondition = false;
    assertEquals(expectedCondition, actualCondition);
  }

  @Test
  void shouldReturnTrueWhenStringIsNullOrEmptyOrBlank() {
    String[] testData = { null, "", " " };
    for (String data : testData) {
      boolean actualCondition = CommonUtil.get().isNullOrEmpty(data);
      boolean expectedCondition = true;
      assertEquals(expectedCondition, actualCondition);
    }
  }

  @Test
  void shouldReturnFalseWhenStringIsNotEmpty() {
    boolean actualCondition = CommonUtil.get().isNullOrEmpty("text");
    boolean expectedCondition = false;
    assertEquals(expectedCondition, actualCondition);
  }

  @Test
  void shouldReturnTrueWhenSomeTextMatchesRegex() {
    String[][] testData = {
        { "/one/url/{pathvar}", Constant.REGEX_PATH_PARAM_URL },
        { "Stream<List<String>>", Constant.REGEX_GENERIC_CLASS }
    };
    for (String[] data : testData) {
      boolean actualMatch = CommonUtil.get().matches(data[0], data[1]);
      boolean expectedMatch = true;
      assertEquals(expectedMatch, actualMatch);
    }
  }

  @Test
  void shouldReturnFalseWhenSomeTextDoesNotMatchRegex() {
    String[][] testData = {
        { "/one/url/pathvar", Constant.REGEX_PATH_PARAM_URL },
        { "CompletableFuture", Constant.REGEX_GENERIC_CLASS }
    };
    for (String[] data : testData) {
      boolean actualMatch = CommonUtil.get().matches(data[0], data[1]);
      boolean expectedMatch = false;
      assertEquals(expectedMatch, actualMatch);
    }
  }

  @Test
  @SuppressWarnings("unchecked")
  void shouldReturnFullMatchesWhenSomeTextMatchesRegex() {
    Object[][] testData = {
        { "/api/service", Constant.REGEX_PATH_PARAM_URL, Arrays.asList() },
        { "/api/service/{path1}", Constant.REGEX_PATH_PARAM_URL, Arrays.asList("path1") },
        { "/api/service/{path1}/{path2}", Constant.REGEX_PATH_PARAM_URL, Arrays.asList("path1", "path2") }
    };
    for (Object[] data : testData) {
      List<String> actualListMatches = CommonUtil.get().findFullMatches((String) data[0], (String) data[1]);
      List<String> expectedListMatches = (List<String>) data[2];
      assertEquals(expectedListMatches, actualListMatches);
    }
  }
}