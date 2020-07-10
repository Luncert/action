package org.luncert.wsaction.argumentresolve;

import org.luncert.wsaction.ActionHandlingContext;
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
