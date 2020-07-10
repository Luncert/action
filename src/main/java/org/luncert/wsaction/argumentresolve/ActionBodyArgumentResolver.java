package org.luncert.wsaction.argumentresolve;

import org.luncert.wsaction.ActionHandlingContext;
import org.luncert.wsaction.annotation.ActionBody;
import org.luncert.wsaction.commons.Constants;
import org.springframework.core.MethodParameter;

public class ActionBodyArgumentResolver implements ActionHandlerMethodArgumentResolver {
  
  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.hasMethodAnnotation(ActionBody.class);
  }
  
  @Override
  public Object resolveArgument(ActionHandlingContext context, MethodParameter parameter) {
    return context.getJsonMessage()
        .getObject(Constants.ACTION_MSG_BODY_KEY, parameter.getParameterType());
  }
}
