package org.luncert.wsaction;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
class ActionHandlerChain {
  
  private List<AbstractActionHandler> actionHandlerList;
  
  ActionHandlerChain(List<AbstractActionHandler> actionHandlers) {
    this.actionHandlerList = actionHandlers;
  }
  
  void handle(String rawMessage) {
    for (AbstractActionHandler actionHandler : actionHandlerList) {
      log.debug("handleData over to handler {}", getHandlerName(actionHandler));
      actionHandler.handleMessage(rawMessage);
    }
  }
  
  private String getHandlerName(AbstractActionHandler actionHandler) {
    if (actionHandler instanceof MethodBasedActionHandler) {
      return actionHandler.toString();
    }
    return actionHandler.getClass().getSimpleName();
  }
}
