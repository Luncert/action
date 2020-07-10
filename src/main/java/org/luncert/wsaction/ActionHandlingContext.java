package org.luncert.wsaction;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.luncert.wsaction.exception.ResolveHandlerMethodArgumentException;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Method;
import java.util.Properties;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActionHandlingContext {
  
  private ApplicationContext springContext;
  
  private ActionSession session;
  
  private String action;
  
  private Properties headers;
  
  private JSONObject jsonMessage;
  
  Object[] resolveHandlerMethodArguments(Method method) {
    try {
      return HandlerMethodArgumentResolverChain.getInstance()
          .resolveMethodParameters(this, method);
    } catch (Exception e) {
      throw new ResolveHandlerMethodArgumentException(e);
    }
  }
}
