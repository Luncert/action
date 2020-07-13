package org.luncert.wsaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Properties;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message<E> {
  
  private String action;
  
  private MessageHeaders headers = new MessageHeaders();
  
  private E body;
}