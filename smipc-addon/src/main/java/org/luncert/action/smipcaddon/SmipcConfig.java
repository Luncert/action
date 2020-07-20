package org.luncert.action.smipcaddon;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.EndianUtils;
import org.luncert.action.core.ActionHandlerManager;
import org.luncert.smipc.Smipc;
import org.luncert.smipc.constants.ChannelMode;
import org.luncert.smipc.constants.LogMode;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;

@Slf4j
@Configuration
public class SmipcConfig {

    private final Smipc.Channel writeChannel;
    private final Smipc.Channel readChannel;
    private final Thread thread;
    private volatile boolean stopSignal = false;

    public SmipcConfig(ActionHandlerManager actionHandlerManager) throws IOException {
        Smipc.init(LogMode.BASIC);
        readChannel = Smipc.open("v1/action/read", ChannelMode.READ, 1);
        writeChannel = Smipc.open("v1/action/write", ChannelMode.WRITE, 1);
        
        // on message
        thread = new Thread(() -> {
            byte[] intBuf = new byte[4];
            byte[] dataBuf;
            try {
                while (!stopSignal) {
                    readChannel.read(intBuf, 0, 4, true);
                    int packageSize = EndianUtils.readSwappedInteger(intBuf, 0);
                    dataBuf = new byte[packageSize];
                    
                    readChannel.read(dataBuf, 0, packageSize, true);
                    actionHandlerManager.handle(new ConnectionSessionImpl(writeChannel), dataBuf);
                }
            } catch (IOException e) {
                log.error("possible exception", e);
            }
            
            synchronized (this) {
                notifyAll();
            }
        });
        thread.setDaemon(true);
    }
    
    @PostConstruct
    public void init() {
        thread.start();
    }

    @PreDestroy
    public void clean() throws InterruptedException {
        stopSignal = true;
        
        synchronized (this) {
            wait();
        }
        
        if (writeChannel != null) {
            writeChannel.close();
        }
        if (readChannel != null) {
            readChannel.close();
        }
    }
}
