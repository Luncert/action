package org.luncert.action.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.luncert.action.core.exception.ResolveHandlerMethodArgumentException;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Method;
import java.nio.ByteBuffer;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActionHandlingContext {

  private ApplicationContext applicationContext;
  
  private ActionSession session;
  
  private Message<ByteBuffer> message;
  
  Object[] resolveHandlerMethodArguments(Method method) {
    try {
      return HandlerMethodArgumentResolverChain.getInstance()
          .resolveMethodParameters(this, method);
    } catch (Exception e) {
      throw new ResolveHandlerMethodArgumentException(e);
    }
  }
}
