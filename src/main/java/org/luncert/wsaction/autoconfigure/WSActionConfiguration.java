package org.luncert.wsaction.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
@ComponentScan("org.luncert.wsaction")
@ConditionalOnBean(ServerEndpointExporter.class)
public class WSActionConfiguration {
}
