package org.luncert.action.core.argumentresolve;

import org.luncert.action.core.ActionHandlingContext;
import org.springframework.core.MethodParameter;

/**
 * This class maybe useless.
 */
@Deprecated
public class SpringContextArgumentResolver implements ActionHandlerMethodArgumentResolver {
  
  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return true;
  }
  
  @Override
  public Object resolveArgument(ActionHandlingContext context, MethodParameter parameter) throws Exception {
    return null;
  }
}
