package org.luncert.wsaction;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.luncert.wsaction.annotation.ActionHandler;
import org.luncert.wsaction.annotation.ActionHandlerRegistry;
import org.luncert.wsaction.exception.MessageTransformException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;

import javax.annotation.PostConstruct;
import javax.websocket.Session;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

// FIXME: is it thread-safe to send data through ws?
@Slf4j
@Component
public final class ActionHandlerManager {
  
  @Autowired
  private ApplicationContext applicationContext;
  
  @Autowired
  private ObjectMapper objectMapper;
  
  private Map<String, ActionHandlerChain> actionHandlerChainMap = new ConcurrentHashMap<>();
  
  @PostConstruct
  public void initFactory() {
    // load all handlers
    List<AbstractActionHandler> handlerList = new ArrayList<>();
    handlerList.addAll(loadAllHandlerImplementations());
    handlerList.addAll(buildHandlerWithRegistry());
    
    log.info("Loaded action handlers: {}", handlerList);
    
    // group by ipcAddress action
    Map<String, List<AbstractActionHandler>> handlerAfterGroupBy = handlerList.stream()
        .collect(Collectors.groupingBy(AbstractActionHandler::getBindingAction));
    
    for (Map.Entry<String, List<AbstractActionHandler>> entry : handlerAfterGroupBy.entrySet()) {
      actionHandlerChainMap.put(entry.getKey(), new ActionHandlerChain(entry.getValue()));
    }
  }
  
  private Collection<AbstractActionHandler> loadAllHandlerImplementations() {
    return applicationContext.getBeansOfType(AbstractActionHandler.class).values();
  }
  
  /**
   * find all class with @ActionHandlerRegistry annotation, and build ActionHandler for their handleData method.
   */
  private List<AbstractActionHandler> buildHandlerWithRegistry() {
    Collection<Object> handlerRegistryCollection =
        applicationContext.getBeansWithAnnotation(ActionHandlerRegistry.class).values();
    
    List<AbstractActionHandler> handlerList = new ArrayList<>();
    
    for (Object registry : handlerRegistryCollection) {
      Class<?> registryType = registry.getClass();
      List<Method> methodList = MethodUtils.getMethodsListWithAnnotation(registryType, ActionHandler.class);
      for (Method method : methodList) {
        verifyMethodSignature(method);
        
        String bindingAction = method.getAnnotation(ActionHandler.class).value();
        handlerList.add(new MethodBasedActionHandler(bindingAction, registry, method));
      }
    }
    
    return handlerList;
  }
  
  /**
   * verify handleData method's signature.
   */
  private void verifyMethodSignature(Method method) {
    //Parameter[] parameters = method.getParameters();
    //int modifiers = method.getModifiers();
    //// verify method modifiers and parameters
    //if (parameters.length > 1
    //    || (parameters.length == 1 &&
    //    (!parameters[0].getType().equals(Message.class)
    //        || !Modifier.isPublic(modifiers)
    //        || Modifier.isStatic(modifiers)))) {
    //  throw new InvalidActionHandlerMethodException(method);
    //}
    
    if (!method.getReturnType().getName().equals("void")) {
      log.warn("Return value of action handler method {}#{} is redundant",
          method.getDeclaringClass().getSimpleName(), method.getName());
    }
  }
  
  void handle(Session session, byte[] byteMsg) {
    Message<ByteBuffer> message;
    try {
      message = MessageTransformer.fromBytes(byteMsg);
    } catch (IOException e) {
      throw new MessageTransformException(e);
    }
    
    ActionHandlerChain chain = actionHandlerChainMap.get(message.getAction());
    if (chain == null) {
      log.warn("No handler registered for action {}", message.getAction());
    } else {
      chain.handle(ActionHandlingContext.builder()
          .session(new ActionSession(session, objectMapper))
          .message(message)
          .build());
    }
  }
}