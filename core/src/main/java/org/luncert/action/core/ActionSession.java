package org.luncert.action.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.luncert.action.core.exception.MessageTransformException;

import java.io.IOException;
import java.nio.ByteBuffer;

public class ActionSession {
  
  private final ConnectionSession session;
  
  private ObjectMapper objectMapper;
  
  ActionSession(ConnectionSession session, ObjectMapper objectMapper) {
    this.session = session;
    this.objectMapper = objectMapper;
  }
  
  public ActionBuilder createAction(String action) {
    return new ActionBuilder(action);
  }
  
  public String getSessionId() {
    return session.getId();
  }
  
  public class ActionBuilder {
    
    private Message<Object> message = new Message<>();
    
    ActionBuilder(String actionName) {
      message.setAction(actionName);
    }
    
    public ActionBuilder addHeader(String key, String value) {
      message.getHeaders().put(key, value);
      return this;
    }
    
    public ActionBuilder body(Object body) {
      message.setBody(body);
      return this;
    }
    
    public void submit() {
      ActionSession.this.sendMessage(message);
    }
  }
  
  private void sendMessage(Message<Object> message) {
    try {
      session.sendBinary(MessageTransformer.toBytes(message, objectMapper));
    } catch (IOException e) {
      throw new MessageTransformException(e);
    }
  }
}
