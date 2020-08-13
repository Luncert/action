package org.luncert.action.core;

import org.luncert.action.core.commons.ActionManageEvent;
import org.luncert.action.core.exception.ActionHandlerException;
import org.luncert.mx1.commons.exception.InvocationError;
import org.luncert.mx1.commons.util.Invokable;

import java.lang.reflect.Method;

public class MethodBasedEventListener extends AbstractEventListener {
  
  private final Invokable handleMethod;
  
  private final Method method;
  
  MethodBasedEventListener(Class<? extends ActionManageEvent> bindingEvent, Object registry, Method method) {
    super(bindingEvent);
    this.handleMethod = new Invokable(registry, method);
    this.method = method;
  }
  
  @Override
  protected void handle(EventHandlingContext context) {
    try {
      handleMethod.apply(context.resolveListenerMethodArguments(method));
    } catch (InvocationError e) {
      throw new ActionHandlerException(e);
    }
  }
  
  @Override
  public String toString() {
    return handleMethod.toString();
  }
}
