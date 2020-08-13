package org.luncert.action.wsaddon;

import lombok.extern.slf4j.Slf4j;
import org.luncert.action.core.ActionHandlerManager;
import org.luncert.action.core.commons.ActionManageEvent;
import org.luncert.action.wsaddon.event.ConnectionClosedEvent;
import org.luncert.action.wsaddon.event.ConnectionEstablishedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

/**
 * WebSocket endpoint.
 * NOTE: @Autowired in web-socket:
 * http://www.programmersought.com/article/2630477574/
 * https://blog.csdn.net/m0_37202351/article/details/86255132
 * TODO: endpoint path should be configurable, create bean manually.
 */
@Slf4j
@Component
@ServerEndpoint(value = "/v1/action")
public class WebSocketEndpoint {

    // FIXME: @Autowired + static doesn't work
    private static ActionHandlerManager actionHandlerManager;

    @Autowired
    public void setActionHandlerManager(ActionHandlerManager actionHandlerManager) {
        WebSocketEndpoint.actionHandlerManager = actionHandlerManager;
    }

    @OnOpen
    public void onOpen(Session session) {
        log.info("connection established, session id={}", session.getId());
        
        ActionManageEvent event = new ConnectionEstablishedEvent();
        event.put("session", new ConnectionSessionImpl(session));
        actionHandlerManager.publishEvent(event);
    }

    @OnClose
    public void onClose(Session session) {
        log.info("connection closed, session id={}", session.getId());
    
        ActionManageEvent event = new ConnectionClosedEvent();
        event.put("session", new ConnectionSessionImpl(session));
        actionHandlerManager.publishEvent(event);
    }

    @OnMessage
    public void onMessage(Session session, byte[] byteMsg) {
        actionHandlerManager.handle(new ConnectionSessionImpl(session), byteMsg);
    }

    @OnError
    public void onError(Throwable e) {
        log.error("WebSocket Error", e);
    }
}
