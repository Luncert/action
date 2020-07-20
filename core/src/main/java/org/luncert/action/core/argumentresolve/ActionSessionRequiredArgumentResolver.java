package org.luncert.action.core.argumentresolve;

import org.luncert.action.core.ActionHandlingContext;
import org.luncert.action.core.ActionSession;
import org.springframework.core.MethodParameter;

public class ActionSessionRequiredArgumentResolver implements ActionHandlerMethodArgumentResolver {
  
  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.getParameterType().equals(ActionSession.class);
  }
  
  @Override
  public Object resolveArgument(ActionHandlingContext context, MethodParameter parameter) throws Exception {
    return context.getSession();
  }
}
