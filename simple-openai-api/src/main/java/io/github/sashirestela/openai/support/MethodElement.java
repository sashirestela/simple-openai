package io.github.sashirestela.openai.support;

import java.lang.reflect.Parameter;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class MethodElement {

  private Parameter parameter;

  private Object defAnnotValue;

  private Object argumentValue;

  // Only this property could be modified
  public void setArgumentValue(Object argumentValue) {
    this.argumentValue = argumentValue;
  }

}