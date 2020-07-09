package org.luncert.wsaction;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.Session;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
public class ActionManager {
  
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
      ActionManager.this.sendMessage(message);
    }
  }
  
  private void sendMessage(Message message) {
    Optional<Session> optionalSession = ActionManagerServer.getSession();
    if (optionalSession.isPresent()) {
      String jsonMsg = JSON.toJSONString(message);
      
      try {
        optionalSession.get().getBasicRemote().sendText(jsonMsg);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    } else {
      log.warn("No session bound to this thread, action {} will be ignored",
          message.getAction());
    }
  }
}