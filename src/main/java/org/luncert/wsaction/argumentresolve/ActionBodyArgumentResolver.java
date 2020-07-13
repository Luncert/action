package org.luncert.wsaction.argumentresolve;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.luncert.wsaction.ActionHandlingContext;
import org.luncert.wsaction.annotation.ActionBody;
import org.luncert.wsaction.exception.ActionHandlerException;
import org.springframework.core.MethodParameter;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.Optional;

public class ActionBodyArgumentResolver implements ActionHandlerMethodArgumentResolver {
  
  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.hasMethodAnnotation(ActionBody.class);
  }
  
  @Override
  public Object resolveArgument(ActionHandlingContext context, MethodParameter parameter) {
    ByteBuffer rawBody = context.getMessage().getBody();
    Collection<ObjectMapper> objectMappers = context.getApplicationContext()
        .getBeansOfType(ObjectMapper.class).values();
    Optional<ObjectMapper> optional = objectMappers.stream().findFirst();
    if (!optional.isPresent()) {
      throw new ActionHandlerException("ObjectMapper not found in spring context, " +
          "thus we are unable to deserialize the message body");
    }
  
    try {
      return optional.get().readValue(rawBody.array(), parameter.getParameterType());
    } catch (IOException e) {
      throw new ActionHandlerException(e);
    }
  }
}
