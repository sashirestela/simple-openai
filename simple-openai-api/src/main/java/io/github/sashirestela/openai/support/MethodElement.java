package io.github.sashirestela.openai.support;

import java.lang.reflect.Parameter;

public class MethodElement {

  private Parameter parameter;
  private Object defAnnotValue;
  private Object argumentValue;

  public MethodElement() {
  }

  public MethodElement(Parameter parameter, Object defAnnotValue, Object argumentValue) {
    this.parameter = parameter;
    this.defAnnotValue = defAnnotValue;
    this.argumentValue = argumentValue;
  }

  public Parameter getParameter() {
    return parameter;
  }

  public Object getDefAnnotValue() {
    return defAnnotValue;
  }

  public Object getArgumentValue() {
    return argumentValue;
  }

  // Only this property could be modified
  public void setArgumentValue(Object argumentValue) {
    this.argumentValue = argumentValue;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    MethodElement other = (MethodElement) obj;
    if (parameter == null) {
      if (other.parameter != null)
        return false;
    } else if (!parameter.equals(other.parameter))
      return false;
    if (defAnnotValue == null) {
      if (other.defAnnotValue != null)
        return false;
    } else if (!defAnnotValue.equals(other.defAnnotValue))
      return false;
    if (argumentValue == null) {
      if (other.argumentValue != null)
        return false;
    } else if (!argumentValue.equals(other.argumentValue))
      return false;
    return true;
  }

}
