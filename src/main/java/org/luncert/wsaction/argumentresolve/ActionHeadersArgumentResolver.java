package org.luncert.wsaction.argumentresolve;

import org.luncert.wsaction.ActionHandlingContext;
import org.luncert.wsaction.MessageHeaders;
import org.luncert.wsaction.annotation.ActionHeaders;
import org.luncert.wsaction.exception.ResolveHandlerMethodArgumentException;
import org.springframework.core.MethodParameter;

import java.util.Properties;

public class ActionHeadersArgumentResolver implements ActionHandlerMethodArgumentResolver {
  
  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.hasMethodAnnotation(ActionHeaders.class);
  }
  
  @Override
  public Object resolveArgument(ActionHandlingContext context, MethodParameter parameter) throws Exception {
    if (MessageHeaders.class.isAssignableFrom(parameter.getParameterType())) {
      return context.getMessage().getHeaders();
    }
    throw new ResolveHandlerMethodArgumentException("parameter with @ActionHeaders annotation must be type of "
        + Properties.class.getName());
  }
}
