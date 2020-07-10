package org.luncert.wsaction.exception;

public class ResolveHandlerMethodArgumentException extends RuntimeException {
  
  public ResolveHandlerMethodArgumentException(Throwable t) {
    super(t);
  }
  
  public ResolveHandlerMethodArgumentException(String message) {
    super(message);
  }
}
