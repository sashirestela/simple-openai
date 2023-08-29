package io.github.sashirestela.openai.http.sender;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.sashirestela.openai.SimpleUncheckedException;
import io.github.sashirestela.openai.http.ReturnType;
import io.github.sashirestela.openai.http.ReturnType.Category;

public class HttpSenderFactory {
  private static Logger LOGGER = LoggerFactory.getLogger(HttpSenderFactory.class);

  private static HttpSenderFactory factory = null;

  private Map<Category, Supplier<HttpSender>> sendersMap;

  private HttpSenderFactory() {
    sendersMap = new HashMap<>();
    sendersMap.put(Category.ASYNC_STREAM, () -> new HttpAsyncStreamSender());
    sendersMap.put(Category.ASYNC_LIST, () -> new HttpAsyncListSender());
    sendersMap.put(Category.ASYNC_OBJECT, () -> new HttpAsyncObjectSender());
    sendersMap.put(Category.ASYNC_PLAIN_TEXT, () -> new HttpAsyncPlainTextSender());
  }

  public static HttpSenderFactory get() {
    if (factory == null) {
      factory = new HttpSenderFactory();
    }
    return factory;
  }

  public HttpSender createSender(ReturnType returnType) {
    HttpSender sender = null;
    Category category = returnType.category();
    if (category != null && sendersMap.containsKey(category)) {
      sender = sendersMap.get(category).get();
      LOGGER.debug("Created Sender : {}", sender.getClass().getSimpleName());
    } else {
      throw new SimpleUncheckedException("Unsupported return type {0}.", returnType.getFullClassName(), null);
    }
    return sender;
  }
}