package org.luncert.action.smipcaddon;

import org.luncert.action.core.ActionHandlerManager;
import org.luncert.smipc.Smipc;
import org.luncert.smipc.constants.ChannelMode;
import org.luncert.smipc.constants.LogMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;
import java.io.IOException;

@Configuration
public class SmipcConfig {

    private final Smipc.Channel writeChannel;
    private final Smipc.Channel readChannel;

    public SmipcConfig(ActionHandlerManager actionHandlerManager) throws IOException {
        Smipc.init(LogMode.BASIC);
        readChannel = Smipc.open("", ChannelMode.READ, 1);
        writeChannel = Smipc.open("", ChannelMode.WRITE, 1);
        // on message
        actionHandlerManager.handle(new ConnectionSessionImpl(), null);
    }

    @Bean
    public Smipc.Channel getWriteChannel() {
        return writeChannel;
    }

    @PreDestroy
    public void clean() {
        if (writeChannel != null) {
            writeChannel.close();
        }
        if (readChannel != null) {
            readChannel.close();
        }
    }
}
