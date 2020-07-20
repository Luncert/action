package org.luncert.action.core;

import org.luncert.action.core.argumentresolve.ActionBodyArgumentResolver;
import org.luncert.action.core.argumentresolve.ActionHandlerMethodArgumentResolver;
import org.luncert.action.core.argumentresolve.ActionHeadersArgumentResolver;
import org.luncert.action.core.argumentresolve.ActionSessionRequiredArgumentResolver;
import org.luncert.action.core.exception.ResolveHandlerMethodArgumentException;
import org.springframework.core.MethodParameter;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;

final class HandlerMethodArgumentResolverChain {
  
  private static final HandlerMethodArgumentResolverChain INSTANCE = new HandlerMethodArgumentResolverChain();
  
  static HandlerMethodArgumentResolverChain getInstance() {
    return INSTANCE;
  }
  
  private List<ActionHandlerMethodArgumentResolver> resolverList;
  
  private HandlerMethodArgumentResolverChain() {
    resolverList = Arrays.asList(
        new ActionSessionRequiredArgumentResolver(),
        new ActionHeadersArgumentResolver(),
        new ActionBodyArgumentResolver()
    );
  }
  
  Object[] resolveMethodParameters(ActionHandlingContext context, Method method) throws Exception {
    Parameter[] parameters = method.getParameters();
    Object[] arguments = new Object[parameters.length];
  
    boolean resolved = false;
    for (int i = 0; i < parameters.length; i++) {
      MethodParameter methodParameter = new MethodParameter(method, i);
      for (ActionHandlerMethodArgumentResolver resolver : resolverList) {
        if (resolver.supportsParameter(methodParameter)) {
          arguments[i] = resolver.resolveArgument(context, methodParameter);
          resolved = true;
          break;
        }
      }
      if (!resolved) {
        throw new ResolveHandlerMethodArgumentException("no qualified object is found for parameter " + methodParameter);
      }
    }
  
    return arguments;
  }
}
