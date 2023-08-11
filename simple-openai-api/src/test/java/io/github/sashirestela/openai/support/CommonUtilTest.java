package io.github.sashirestela.openai.support;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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
    String[] testData = {null, "", " "};
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
  void shouldReturnMatchingTextWhenSomeTextMatchesRegex() {
    String[][] testData = {
        { "/one/url/{pathvar}", Constant.REGEX_PATH_PARAM_URL, "pathvar" },
        { "Stream<String>", Constant.REGEX_GENERIC_CLASS, "String" },
        { "Stream<List<String>>", Constant.REGEX_GENERIC_CLASS, "List<String>" }
    };
    for (String[] data : testData) {
      String actualMatchingText = CommonUtil.get().findFirstMatch(data[0], data[1]);
      String expectedMatchingText = data[2];
      assertEquals(expectedMatchingText, actualMatchingText);
    }
  }

  @Test
  void shouldReturnNullWhenSomeTextDoesNotMatchRegex() {
    String[][] testData = {
        { "/one/url/pathvar", Constant.REGEX_PATH_PARAM_URL },
        { "String", Constant.REGEX_GENERIC_CLASS }
    };
    for (String[] data : testData) {
      assertNull(CommonUtil.get().findFirstMatch(data[0], data[1]));
    }
  }

  @Test
  void shouldReturnInnerGroupWhenPassingAnyTextAndRegex() {
    String[][] testData = {
        { "String", Constant.REGEX_GENERIC_CLASS, "String" },
        { "Stream<Double>", Constant.REGEX_GENERIC_CLASS, "Double" },
        { "Stream<List<Integer>>", Constant.REGEX_GENERIC_CLASS, "Integer" }
    };
    for (String[] data : testData) {
      String actualMatchingGroup = CommonUtil.get().findInnerGroup(data[0], data[1]);
      String expectedMatchingGroup = data[2];
      assertEquals(expectedMatchingGroup, actualMatchingGroup);
    }
  }

  @Test
  @SuppressWarnings("unchecked")
  void shouldReturnAllMatchesWhenSomeTextMatchesRegex() {
    Object[][] testData = {
        { "/api/service", Constant.REGEX_PATH_PARAM_URL, Arrays.asList() },
        { "/api/service/{path1}", Constant.REGEX_PATH_PARAM_URL, Arrays.asList(
            new CommonUtil.Match("{path1}", "path1")) },
        { "/api/service/{path1}/{path2}", Constant.REGEX_PATH_PARAM_URL, Arrays.asList(
            new CommonUtil.Match("{path1}", "path1"),
            new CommonUtil.Match("{path2}", "path2")) }
    };
    for (Object[] data : testData) {
      List<CommonUtil.Match> actualListMatches = CommonUtil.get().findAllMatches((String) data[0], (String) data[1]);
      List<CommonUtil.Match> expectedListMatches = (List<CommonUtil.Match>) data[2];
      assertEquals(expectedListMatches, actualListMatches);
    }
  }
}