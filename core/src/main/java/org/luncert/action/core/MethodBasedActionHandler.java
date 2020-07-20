package org.luncert.action.core;

import org.luncert.mx1.commons.exception.InvocationError;
import org.luncert.mx1.commons.util.Invokable;
import org.luncert.action.core.exception.ActionHandlerException;

import java.lang.reflect.Method;

public class MethodBasedActionHandler extends AbstractActionHandler {
  
  private final Invokable handleMethod;
  
  private final Method method;
  
  MethodBasedActionHandler(String bindingAction, Object registry, Method method) {
    super(bindingAction);
    this.handleMethod = new Invokable(registry, method);
    this.method = method;
  }
  
  @Override
  protected void handle(ActionHandlingContext context) {
    try {
      handleMethod.apply(context.resolveHandlerMethodArguments(method));
    } catch (InvocationError e) {
      throw new ActionHandlerException(e);
    }
  }
  
  @Override
  public String toString() {
    return handleMethod.toString();
  }
}