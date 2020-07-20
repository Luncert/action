package org.luncert.action.smipcaddon;

import org.luncert.action.core.ConnectionSession;
import org.luncert.smipc.Smipc;

import java.io.IOException;

class ConnectionSessionImpl implements ConnectionSession {
    
    private Smipc.Channel writeChannel;
    
    ConnectionSessionImpl(Smipc.Channel writeChannel) {
        this.writeChannel = writeChannel;
    }

    @Override
    public String getId() {
        return this.getClass().getName();
    }

    @Override
    public void sendBinary(byte[] data) throws IOException {
        writeChannel.write(data, 0, data.length);
    }
}
