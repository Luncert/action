package org.luncert.action.core.commons;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ActionManageEvent implements Serializable {
  
  private static final long serialVersionUID = 3715821978074924137L;
  
  private Map<String, Object> data = new HashMap<>();
  
  public void put(String key, Object value) {
    data.put(key, value);
  }
  
  public Object get(String key) {
    return data.get(key);
  }
}
