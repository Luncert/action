package org.luncert.action.core.argumentresolve;

import org.luncert.action.core.ActionHandlingContext;
import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;

public interface ActionHandlerMethodArgumentResolver {
  
  boolean supportsParameter(MethodParameter parameter);
  
  @Nullable
  Object resolveArgument(ActionHandlingContext context, MethodParameter parameter) throws Exception;
}
