package org.luncert.wsaction;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.luncert.wsaction.exception.MessageTransformException;

import javax.websocket.Session;
import java.io.IOException;
import java.nio.ByteBuffer;

public class ActionSession {
  
  private final Session session;
  
  private ObjectMapper objectMapper;
  
  ActionSession(Session session, ObjectMapper objectMapper) {
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
      session.getBasicRemote().sendBinary(
          ByteBuffer.wrap(MessageTransformer.toBytes(message, objectMapper)));
    } catch (IOException e) {
      throw new MessageTransformException(e);
    }
  }
}
