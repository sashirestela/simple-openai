package io.github.sashirestela.openai.filter;

import java.lang.reflect.Method;
import java.util.EnumSet;

import io.github.sashirestela.openai.SimpleUncheckedException;
import io.github.sashirestela.openai.domain.audio.AudioRespFmt;
import io.github.sashirestela.openai.domain.audio.AudioTranslateRequest;
import io.github.sashirestela.openai.support.ReflectUtil;

public class AudioFilter implements FilterInvocation {
  private final static EnumSet<AudioRespFmt> JSON_FMT = EnumSet.of(AudioRespFmt.JSON, AudioRespFmt.VERBOSE_JSON);
  private final static EnumSet<AudioRespFmt> TEXT_FMT = EnumSet.complementOf(JSON_FMT);

  @Override
  public void filterArguments(Object proxy, Method method, Object[] arguments) {
    Class<?> responseClass = ReflectUtil.get().getBaseClass(method);
    AudioTranslateRequest requestObj = (AudioTranslateRequest) arguments[0];
    AudioRespFmt responseFormat = requestObj.getResponseFormat();
    
    if (responseClass.getSimpleName().equals("String")) {
      if (responseFormat != null) {
        if (!TEXT_FMT.contains(responseFormat)) {
          throw new SimpleUncheckedException("Unexpected responseFormat for the method {0}.", method.getName(), null);
        }
      } else {
        requestObj.setResponseFormat(AudioRespFmt.TEXT);
      }
    } else {
      if (responseFormat != null) {
        if (!JSON_FMT.contains(responseFormat)) {
          throw new SimpleUncheckedException("Unexpected responseFormat for the method {0}.", method.getName(), null);
        }
      } else {
        requestObj.setResponseFormat(AudioRespFmt.JSON);
      }
    }
  }
}