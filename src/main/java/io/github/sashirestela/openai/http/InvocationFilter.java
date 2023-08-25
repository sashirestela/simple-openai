package io.github.sashirestela.openai.http;

import java.lang.reflect.Method;

public interface InvocationFilter {

  void apply(Method method, Object[] arguments);

}