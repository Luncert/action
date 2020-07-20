package org.luncert.action.wsaddon;

import lombok.extern.slf4j.Slf4j;
import org.luncert.action.core.ActionHandlerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

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
    }

    @OnClose
    public void onClose(Session session) {
        log.info("connection closed, session id={}", session.getId());
    }

    @OnMessage
    public void onMessage(Session session, String rawMsg) {
        actionHandlerManager.handle(new ConnectionSessionImpl(session), rawMsg.getBytes());
    }

    @OnError
    public void onError(Throwable e) {
        log.error("WebSocket Error", e);
    }
}
