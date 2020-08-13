package org.luncert.action.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.luncert.action.core.annotation.ActionHandler;
import org.luncert.action.core.annotation.ActionHandlerRegistry;
import org.luncert.action.core.annotation.ActionManageEventListener;
import org.luncert.action.core.commons.ActionManageEvent;
import org.luncert.action.core.exception.MessageTransformException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
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
  
  private final ApplicationContext applicationContext;
  
  private final ObjectMapper objectMapper;
  
  // actionName -> actionHandlerChain
  private Map<String, ActionHandlerChain> actionHandlerChainMap = new ConcurrentHashMap<>();
  
  // actionManageEvent -> listenerChain
  private Map<Class<? extends ActionManageEvent>, ActionManageEventListenerChain> actionEventListenerChainMap
      = new ConcurrentHashMap<>();

  public ActionHandlerManager(ApplicationContext applicationContext, ObjectMapper objectMapper) {
    this.applicationContext = applicationContext;
    this.objectMapper = objectMapper;
  }

  @PostConstruct
  public void initFactory() {
    loadActionHandlers();
  }
  
  private void loadActionHandlers() {
    // load all handlers and event listeners
    List<AbstractEventListener> listenerList = new ArrayList<>();
    List<AbstractActionHandler> handlerList = new ArrayList<>(loadAllHandlerImplementations());
    
    // process handler registry
    Collection<Object> handlerRegistryCollection =
        applicationContext.getBeansWithAnnotation(ActionHandlerRegistry.class).values();
  
    for (Object registry : handlerRegistryCollection) {
      Class<?> registryType = registry.getClass();
    
      // get all methods with annotation @ActionHandler
      List<Method> methodList = MethodUtils.getMethodsListWithAnnotation(registryType, ActionHandler.class);
      for (Method method : methodList) {
        verifyMethodSignature(method);
      
        String bindingAction = method.getAnnotation(ActionHandler.class).value();
        handlerList.add(new MethodBasedActionHandler(bindingAction, registry, method));
      }
    
      // get all methods with annotation @ActionManageEventListener
      methodList = MethodUtils.getMethodsListWithAnnotation(registryType, ActionManageEventListener.class);
      for (Method method : methodList) {
        Class<? extends ActionManageEvent> bindingEvent = method.getAnnotation(ActionManageEventListener.class).value();
        listenerList.add(new MethodBasedEventListener(bindingEvent, registry, method));
      }
    }
  
    log.info("Loaded ActionHandler: {}", handlerList);
    log.info("Loaded ActionManageEventListener: {}", handlerList);
  
    // group handlers by action
    Map<String, List<AbstractActionHandler>> handlerAfterGroupBy = handlerList.stream()
        .collect(Collectors.groupingBy(AbstractActionHandler::getBindingAction));
  
    for (Map.Entry<String, List<AbstractActionHandler>> entry : handlerAfterGroupBy.entrySet()) {
      actionHandlerChainMap.put(entry.getKey(), new ActionHandlerChain(entry.getValue()));
    }
    
    // group listeners by event
    Map<Class<? extends ActionManageEvent>, List<AbstractEventListener>> listenerAfterGroupBy = listenerList.stream()
        .collect(Collectors.groupingBy(AbstractEventListener::getBindingEvent));
  
    for (Map.Entry<Class<? extends ActionManageEvent>, List<AbstractEventListener>> entry : listenerAfterGroupBy.entrySet()) {
      actionEventListenerChainMap.put(entry.getKey(), new ActionManageEventListenerChain(entry.getValue()));
    }
  }
  
  private Collection<AbstractActionHandler> loadAllHandlerImplementations() {
    return applicationContext.getBeansOfType(AbstractActionHandler.class).values();
  }
  
  /**
   * verify handleData method's signature.
   */
  private void verifyMethodSignature(Method method) {
    if (!method.getReturnType().getName().equals("void")) {
      log.warn("Return value of action handler method {}#{} is redundant",
          method.getDeclaringClass().getSimpleName(), method.getName());
    }
  }
  
  public void publishEvent(ActionManageEvent event) {
    ActionManageEventListenerChain chain = actionEventListenerChainMap.get(event.getClass());
    if (chain == null) {
      log.debug("No listener subscribed event {}", event);
    } else {
      chain.handle(new EventHandlingContext(event, objectMapper));
    }
  }
  
  public void handle(ConnectionSession session, byte[] rawMessage) {
    Message<ByteBuffer> message;
    try {
      message = MessageTransformer.fromBytes(rawMessage);
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