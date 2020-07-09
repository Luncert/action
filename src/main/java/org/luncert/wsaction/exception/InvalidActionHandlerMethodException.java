package org.luncert.wsaction.exception;

import java.lang.reflect.Method;
import java.text.MessageFormat;

public class InvalidActionHandlerMethodException extends ActionHandlerException {
  
  public InvalidActionHandlerMethodException(Method method) {
    super(MessageFormat.format("Invalid action handler method {0}#{1}," +
            " expect: public void handleMethod(Message<E> message)",
        method.getDeclaringClass().getSimpleName(), method.getName()));
  }
}
