package org.luncert.wsaction.argumentresolve;

import org.luncert.wsaction.ActionHandlingContext;
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
    if (Properties.class.isAssignableFrom(parameter.getParameterType())) {
      return context.getHeaders();
    }
    throw new ResolveHandlerMethodArgumentException("parameter with @ActionHeaders annotation must be type of "
        + Properties.class.getName());
  }
}
