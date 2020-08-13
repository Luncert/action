package org.luncert.action.core;

import lombok.Getter;
import org.luncert.action.core.commons.ActionManageEvent;

abstract class AbstractEventListener {
  
  @Getter
  private Class<? extends ActionManageEvent> bindingEvent;
  
  AbstractEventListener(Class<? extends ActionManageEvent> bindingEvent) {
    this.bindingEvent = bindingEvent;
  }
  
  protected abstract void handle(EventHandlingContext context);
}
