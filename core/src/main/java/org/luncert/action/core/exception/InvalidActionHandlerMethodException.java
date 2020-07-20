package org.luncert.action.core.exception;

import java.lang.reflect.Method;
import java.text.MessageFormat;

public class InvalidActionHandlerMethodException extends ActionHandlerException {
  
  public InvalidActionHandlerMethodException(Method method) {
    super(MessageFormat.format("Invalid action handler method {0}#{1}," +
            " expect: public void handleMethod(Message<E> message) or public void handleMethod()",
        method.getDeclaringClass().getSimpleName(), method.getName()));
  }
}
