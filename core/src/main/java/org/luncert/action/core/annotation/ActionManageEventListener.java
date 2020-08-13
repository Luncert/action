package org.luncert.action.core.annotation;

import org.luncert.action.core.commons.ActionManageEvent;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ActionManageEventListener {
  
  Class<? extends ActionManageEvent> value();
}
