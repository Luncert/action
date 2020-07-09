package org.luncert.wsaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Properties;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Message<E> {
  
  private String action;
  
  private Properties headers = new Properties();
  
  private E body;
}