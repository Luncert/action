package org.luncert.wsaction.argumentresolve;

import org.luncert.wsaction.ActionHandlingContext;
import org.luncert.wsaction.ActionSession;
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
