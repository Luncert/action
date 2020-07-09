package org.luncert.wsaction;

import org.luncert.mx1.commons.exception.InvocationError;
import org.luncert.mx1.commons.util.Invokable;
import org.luncert.wsaction.exception.ActionHandlerException;

import java.lang.reflect.Method;

public class MethodBasedActionHandler extends AbstractActionHandler {
  
  private Invokable handleMethod;
  
  MethodBasedActionHandler(String action, Object registry, Method method) {
    super(action, resolveMessageBodyType(method));
    this.handleMethod = new Invokable(registry, method);
  }
  
  @Override
  protected void handle(Message message) {
    try {
      handleMethod.apply(message);
    } catch (InvocationError e) {
      throw new ActionHandlerException(e);
    }
  }
  
  @Override
  public String toString() {
    return handleMethod.toString();
  }
}