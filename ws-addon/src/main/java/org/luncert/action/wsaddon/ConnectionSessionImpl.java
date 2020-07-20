package org.luncert.action.wsaddon;

import org.luncert.action.core.ConnectionSession;

import javax.websocket.Session;
import java.io.IOException;
import java.nio.ByteBuffer;

class ConnectionSessionImpl implements ConnectionSession {

    private Session session;

    ConnectionSessionImpl(Session session) {
        this.session = session;
    }

    @Override
    public String getId() {
        return session.getId();
    }

    @Override
    public void sendBinary(byte[] data) throws IOException {
        session.getBasicRemote().sendBinary(ByteBuffer.wrap(data));
    }
}
