package com.nodemessage.jooby.web.auconfig.annotation;

import io.jooby.ExecutionMode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static io.jooby.ExecutionMode.EVENT_LOOP;

/**
 * @author wjsmc
 * @date 2022/8/7 22:32
 * @description
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface JoobyPlusStarter {
    ExecutionMode EXECUTION_MODE() default EVENT_LOOP;
}
