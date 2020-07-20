package org.luncert.wsaction;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 * WebSocket endpoint.
 * NOTE: @Autowired in web-socket:
 * http://www.programmersought.com/article/2630477574/
 * https://blog.csdn.net/m0_37202351/article/details/86255132
 * TODO: endpoint path should be configurable
 */
@Slf4j
@Component
@ServerEndpoint(value = "/v1/action")
public class ActionManagerServer {
  
  // FIXME: @Autowired + static doesn't work
  private static ActionHandlerManager actionHandlerManager;
  
  @Autowired
  public void setActionHandlerManager(ActionHandlerManager actionHandlerManager) {
    ActionManagerServer.actionHandlerManager = actionHandlerManager;
  }
  
  @OnOpen
  public void onOpen(Session session) {
    log.info("connection established, session id={}", session.getId());
  }
  
  @OnClose
  public void onClose(Session session) {
    log.info("connection closed, session id={}", session.getId());
  }
  
  @OnMessage
  public void onMessage(Session session, byte[] byteMsg) {
    actionHandlerManager.handle(session, byteMsg);
  }
  
  @OnError
  public void onError(Throwable e) {
    log.error("WebSocket Error", e);
  }
}
