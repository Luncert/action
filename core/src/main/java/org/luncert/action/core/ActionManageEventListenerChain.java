package org.luncert.action.core;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
class ActionManageEventListenerChain {
  
  private List<AbstractEventListener> eventListeners;
  
  ActionManageEventListenerChain(List<AbstractEventListener> eventListeners) {
    this.eventListeners = eventListeners;
  }
  
  void handle(EventHandlingContext context) {
    for (AbstractEventListener listener : eventListeners) {
      listener.handle(context);
    }
  }
}
