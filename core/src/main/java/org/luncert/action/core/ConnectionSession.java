package org.luncert.action.core;

import java.io.IOException;

public interface ConnectionSession {

    String getId();

    void sendBinary(byte[] data) throws IOException;
}
