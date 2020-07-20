package org.luncert.action.core;

import lombok.Getter;

abstract class AbstractActionHandler {
  @Getter
  private final String bindingAction;
  
  AbstractActionHandler(String bindingAction) {
    this.bindingAction = bindingAction;
  }
  
  protected abstract void handle(ActionHandlingContext context);
}
