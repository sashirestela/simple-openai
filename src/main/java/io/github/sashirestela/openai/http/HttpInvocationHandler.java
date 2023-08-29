package io.github.sashirestela.openai.http;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpInvocationHandler implements InvocationHandler {
  private static Logger LOGGER = LoggerFactory.getLogger(HttpInvocationHandler.class);

  private HttpProcessor processor;
  private InvocationFilter filter;

  public HttpInvocationHandler(HttpProcessor processor, InvocationFilter filter) {
    this.processor = processor;
    this.filter = filter;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] arguments) throws Throwable {
    LOGGER.debug("Invoked Method : {}.{}()", method.getDeclaringClass().getSimpleName(), method.getName());
    if (filter != null) {
      filter.apply(method, arguments);
      LOGGER.debug("Applied Filter : {}", filter.getClass().getSimpleName());
    }
    Object responseObject = processor.resolve(method, arguments);
    LOGGER.debug("Received Response");

    return responseObject;
  }

}