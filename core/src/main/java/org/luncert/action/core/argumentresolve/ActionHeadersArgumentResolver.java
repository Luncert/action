package org.luncert.action.core.argumentresolve;

import org.luncert.action.core.ActionHandlingContext;
import org.luncert.action.core.MessageHeaders;
import org.luncert.action.core.annotation.ActionHeaders;
import org.luncert.action.core.exception.ResolveHandlerMethodArgumentException;
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
