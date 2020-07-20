package org.luncert.action.smipcaddon;

import org.luncert.action.core.ConnectionSession;

import java.io.IOException;

class ConnectionSessionImpl implements ConnectionSession {

    @Override
    public String getId() {
        return this.getClass().getName();
    }

    @Override
    public void sendBinary(byte[] data) throws IOException {

    }
}
