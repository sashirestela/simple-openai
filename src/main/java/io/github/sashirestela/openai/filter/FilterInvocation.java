package io.github.sashirestela.openai.filter;

import java.lang.reflect.Method;

public interface FilterInvocation {

  void filterArguments(Object proxy, Method method, Object[] arguments);

}