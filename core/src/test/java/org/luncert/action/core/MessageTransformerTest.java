package org.luncert.action.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.nio.ByteBuffer;

@RunWith(JUnit4.class)
public class MessageTransformerTest {
  
  @Test
  public void successCase() throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    
    Message<Object> msg = new Message<>();
    msg.setAction("action");
    msg.getHeaders().put("header1", "value1");
    msg.setBody(1500);
    
    byte[] bytes = MessageTransformer.toBytes(msg, objectMapper);
    Message<ByteBuffer> transformedMessage = MessageTransformer.fromBytes(bytes);
    Assert.assertEquals(msg.getAction(), transformedMessage.getAction());
    Assert.assertEquals(msg.getHeaders(), transformedMessage.getHeaders());
    
    Integer body = objectMapper.readValue(transformedMessage.getBody().array(), Integer.class);
    Assert.assertEquals(msg.getBody(), body);
  }
}
