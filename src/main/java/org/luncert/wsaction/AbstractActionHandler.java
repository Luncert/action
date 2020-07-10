package org.luncert.wsaction;

import lombok.Getter;

abstract class AbstractActionHandler<E> {
  @Getter
  private final String bindingAction;
  
  AbstractActionHandler(String bindingAction) {
    this.bindingAction = bindingAction;
  }
  
  protected abstract void handle(ActionHandlingContext context);
}
