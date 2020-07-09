package org.luncert.wsaction;

import com.alibaba.fastjson.JSON;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.type.TypeDescription;
import org.luncert.wsaction.exception.ActionHandlerException;
import org.luncert.wsaction.exception.InvalidActionHandlerMethodException;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class AbstractActionHandler<E> {
  
  private final String bindingAction;
  
  private final Type messageType;
  
  public AbstractActionHandler(String bindingAction) {
    this.bindingAction = bindingAction;
    
    // detect message body type through type argument of implementation of handleData method.
    Method handleMethod;
    
    try {
      handleMethod = getClass().getDeclaredMethod("handle", Message.class);
    } catch (NoSuchMethodException e) {
      throw new ActionHandlerException(e);
    }
    
    Type messageBodyType = resolveMessageBodyType(handleMethod);
    messageType = buildMessageType(messageBodyType);
  }
  
  /**
   * Resolve message body type from handler method argument by reflection.
   * @param method handler method
   * @return message body type
   */
  static Type resolveMessageBodyType(Method method) {
    Type referenceType = method.getGenericParameterTypes()[0];
    
    if (!(referenceType instanceof ParameterizedType)) {
      throw new InvalidActionHandlerMethodException(method);
    }
    
    Type[] actualTypeArgs = ((ParameterizedType) referenceType).getActualTypeArguments();
    
    if (actualTypeArgs.length != 1) {
      throw new InvalidActionHandlerMethodException(method);
    }
    
    return actualTypeArgs[0];
  }
  
  // for MethodBasedActionHandler
  AbstractActionHandler(String bindingAction, Type messageBodyType) {
    this.bindingAction = bindingAction;
    messageType = buildMessageType(messageBodyType);
  }
  
  private Class<?> buildMessageType(Type messageBodyType) {
    // build a new message type used by fastjson to resolve ws message according to actual handler
    return new ByteBuddy()
        .subclass(TypeDescription.Generic.Builder
            .parameterizedType(Message.class, messageBodyType)
            .build())
        .make()
        .load(Message.class.getClassLoader())
        .getLoaded();
  }
  
  /**
   * Deserialization string message before handling each time.
   */
  void handleMessage(String rawMessage) {
    // deserialization referring messageBodyType
    // invoke handleData implementation
    handle(JSON.parseObject(rawMessage, messageType));
  }
  
  protected abstract void handle(Message<E> message);
  
  String getBindingAction() {
    return bindingAction;
  }
}
