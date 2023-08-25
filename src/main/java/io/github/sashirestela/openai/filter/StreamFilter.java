package io.github.sashirestela.openai.filter;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import io.github.sashirestela.openai.http.InvocationFilter;
import io.github.sashirestela.openai.http.ResponseType;
import io.github.sashirestela.openai.support.ReflectUtil;

public class StreamFilter implements InvocationFilter {

  @Override
  public void apply(Method method, Object[] arguments) {
    ResponseType responseType = ReflectUtil.get().getResponseType(method);
    boolean isStreaming = (responseType == ResponseType.STREAM);
    final String SET_STREAM_METHOD = "setStream";
    Parameter parameter = method.getParameters()[0];
    Object object = arguments[0];
    ReflectUtil.get().executeSetMethod(parameter.getType(), SET_STREAM_METHOD, new Class<?>[] { boolean.class }, object,
        isStreaming);
  }
}
