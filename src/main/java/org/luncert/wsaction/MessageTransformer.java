package org.luncert.wsaction;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.EndianUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;

final class MessageTransformer {
  
  static byte[] toBytes(Message message, ObjectMapper objectMapper) throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
  
    // write action
    EndianUtils.writeSwappedInteger(outputStream, message.getAction().length());
    outputStream.write(message.getAction().getBytes());
  
    // write headers
    EndianUtils.writeSwappedInteger(outputStream, message.getHeaders().size());
    for (Map.Entry<String, String> header : message.getHeaders().entrySet()) {
      EndianUtils.writeSwappedInteger(outputStream, header.getKey().length());
      outputStream.write(header.getKey().getBytes());
      EndianUtils.writeSwappedInteger(outputStream, header.getValue().length());
      outputStream.write(header.getValue().getBytes());
    }
  
    // write body
    byte[] body = objectMapper.writeValueAsBytes(message.getBody());
    EndianUtils.writeSwappedInteger(outputStream, body.length);
    outputStream.write(body);
    
    return outputStream.toByteArray();
  }
  
  static Message<ByteBuffer> fromBytes(byte[] raw) throws IOException {
    Message<ByteBuffer> message = new Message<>();
  
    ByteArrayInputStream inputStream = new ByteArrayInputStream(raw);
    byte[] buffer = new byte[raw.length];
    
    // read action
    int len = EndianUtils.readSwappedInteger(inputStream);
    int n = inputStream.read(buffer, 0, len);
    assertN(len, n);
    message.setAction(new String(buffer, 0, len));
    
    // read headers
    int headerSize = EndianUtils.readSwappedInteger(inputStream);
    MessageHeaders messageHeaders = message.getHeaders();
    String key, value;
    for (int i = 0; i < headerSize; i++) {
      len = EndianUtils.readSwappedInteger(inputStream);
      n = inputStream.read(buffer, 0, len);
      assertN(len, n);
      key = new String(buffer, 0, len);
  
      len = EndianUtils.readSwappedInteger(inputStream);
      n = inputStream.read(buffer, 0, len);
      assertN(len, n);
      value = new String(buffer, 0, len);
      
      messageHeaders.put(key, value);
    }
    
    // read body
    len = EndianUtils.readSwappedInteger(inputStream);
    n = inputStream.read(buffer, 0, len);
    assertN(len, n);
    
    ByteBuffer rawBody = ByteBuffer.allocate(len);
    rawBody.put(buffer, 0, len);
    message.setBody(rawBody);
    
    return message;
  }
  
  private static void assertN(int expect, int n) throws IOException {
    if (n != expect) {
      throw new IOException("expect to read " + expect + " bytes, actually got " + n);
    }
  }
}
