package org.luncert.action.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.luncert.action.core.annotation.EventData;
import org.luncert.action.core.commons.ActionManageEvent;
import org.luncert.action.core.exception.ResolveListenerMethodArgumentException;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@Data
@AllArgsConstructor
class EventHandlingContext {
  
  private final ActionManageEvent event;
  
  private final ObjectMapper objectMapper;
  
  Object[] resolveListenerMethodArguments(Method method) {
    Parameter[] parameters = method.getParameters();
    Object[] arguments = new Object[parameters.length];
    
    for (int i = 0; i < parameters.length; i++) {
      Parameter param = parameters[i];
      EventData eventData = param.getAnnotation(EventData.class);
      String paramName = eventData.value();
      Object providedParam = event.get(paramName);

      if (providedParam == null) {
        throw new ResolveListenerMethodArgumentException("no argument found with name " + paramName
            + " when handling " + method.getDeclaringClass() + "#" + method.getName());
      } else if (param.getType().equals(providedParam.getClass())) {
        arguments[i] = providedParam;
      } else if (param.getType().equals(ActionSession.class) && (providedParam instanceof ConnectionSession)) {
        // special scenario for ActionSession
        arguments[i] = new ActionSession((ConnectionSession) providedParam, objectMapper);
      } else {
        try {
          arguments[i] = param.getType().cast(providedParam);
        } catch (ClassCastException e) {
          throw new ResolveListenerMethodArgumentException(
              "type " + providedParam.getClass() + " cannot be cast to " + param.getType() + " for param " + paramName
                  + " when handling " + method.getDeclaringClass() + "#" + method.getName());
        }
      }
    }
    
    return arguments;
  }
}
