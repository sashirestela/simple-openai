package io.github.sashirestela.openai.http;

public enum ResponseType {
  OBJECT(""),
  LIST("java.util.List"),
  STREAM("java.util.stream.Stream"),
  UNKNOWN("*");

  private String className;

  private ResponseType(String className) {
    this.className = className;
  }

  public String getClassName() {
    return className;
  }
}