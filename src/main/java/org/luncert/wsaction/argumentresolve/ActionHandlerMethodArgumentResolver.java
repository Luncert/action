package org.luncert.wsaction.argumentresolve;

import org.luncert.wsaction.ActionHandlingContext;
import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;

public interface ActionHandlerMethodArgumentResolver {
  
  boolean supportsParameter(MethodParameter parameter);
  
  @Nullable
  Object resolveArgument(ActionHandlingContext context, MethodParameter parameter) throws Exception;
}
