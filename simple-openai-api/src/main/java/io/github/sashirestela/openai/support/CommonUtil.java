package io.github.sashirestela.openai.support;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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

  public static class Match {
    private String textToReplace;
    private String textToSearch;

    public Match(String textToReplace, String textToSearch) {
      this.textToReplace = textToReplace;
      this.textToSearch = textToSearch;
    }

    public String getTextToReplace() {
      return textToReplace;
    }

    public String getTextToSearch() {
      return textToSearch;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      Match other = (Match) obj;
      if (textToReplace == null) {
        if (other.textToReplace != null)
          return false;
      } else if (!textToReplace.equals(other.textToReplace))
        return false;
      if (textToSearch == null) {
        if (other.textToSearch != null)
          return false;
      } else if (!textToSearch.equals(other.textToSearch))
        return false;
      return true;
    }

  }
}
