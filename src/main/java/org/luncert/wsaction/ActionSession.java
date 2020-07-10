package org.luncert.wsaction;

import com.alibaba.fastjson.JSON;

import javax.websocket.Session;
import java.io.IOException;

public class ActionSession {
  
  private final Session session;
  
  ActionSession(Session session) {
    this.session = session;
  }
  
  public ActionBuilder createAction(String action) {
    return new ActionBuilder();
  }
  
  public class ActionBuilder {
    
    private Message<Object> message = new Message<>();
    
    
    public ActionBuilder addHeader(String key, Object value) {
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
  
  private void sendMessage(Message message) {
    String jsonMsg = JSON.toJSONString(message);
    try {
      session.getBasicRemote().sendText(jsonMsg);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
