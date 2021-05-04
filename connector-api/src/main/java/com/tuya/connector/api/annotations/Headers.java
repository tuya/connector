package com.tuya.connector.api.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author qiufeng.yu@tuya.com
 */
@Documented
@Target(METHOD)
@Retention(RUNTIME)
public @interface Headers {
  String[] value();
}
