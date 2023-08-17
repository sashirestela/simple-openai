package io.github.sashirestela.openai.support;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

public class CommonUtil {
  private static CommonUtil commonUtil = null;

  private CommonUtil() {
  }

  public static CommonUtil get() {
    if (commonUtil == null) {
      commonUtil = new CommonUtil();
    }
    return commonUtil;
  }

  public boolean isNullOrEmpty(Object[] array) {
    return array == null || array.length == 0;
  }

  public boolean isNullOrEmpty(List<?> list) {
    return list == null || list.size() == 0;
  }

  public boolean isNullOrEmpty(String text) {
    return text == null || text.isBlank();
  }

  public boolean matches(String text, String regex) {
    Matcher matcher = Pattern.compile(regex).matcher(text);
    return matcher.find();
  }

  public String findFirstMatch(String text, String regex) {
    Matcher matcher = Pattern.compile(regex).matcher(text);
    String value = matcher.find() ? matcher.group(1) : null;
    return value;
  }

  public String findInnerGroup(String text, String regex) {
    Matcher matcher = Pattern.compile(regex).matcher(text);
    String value = matcher.find() ? findInnerGroup(matcher.group(1), regex) : text;
    return value;
  }

  public List<Match> findAllMatches(String text, String regex) {
    Matcher matcher = Pattern.compile(regex).matcher(text);
    List<Match> result = matcher.results()
        .map(mr -> new Match(mr.group(0), mr.group(1)))
        .collect(Collectors.toList());
    return result;
  }

  public String capitalize(String text) {
    return text.substring(0, 1).toUpperCase() + text.substring(1);
  }

  public <T> T[] concatArrays(T[] array1, T[] array2) {
    T[] result = Arrays.copyOf(array1, array1.length + array2.length);
    System.arraycopy(array2, 0, result, array1.length, array2.length);
    return result;
  }

  @AllArgsConstructor
  @Getter
  @EqualsAndHashCode
  public static class Match {
    
    private String textToReplace;
    
    private String textToSearch;

  }
}