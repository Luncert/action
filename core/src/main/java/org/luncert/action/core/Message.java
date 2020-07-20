package org.luncert.action.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message<E> {
  
  private String action;
  
  private MessageHeaders headers = new MessageHeaders();
  
  private E body;
}