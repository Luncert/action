package org.luncert.wsaction.exception;

public class ActionHandlerException extends RuntimeException {
  
  public ActionHandlerException(String message) {
    super(message);
  }
  
  public ActionHandlerException(Throwable cause) {
    super(cause);
  }
}
