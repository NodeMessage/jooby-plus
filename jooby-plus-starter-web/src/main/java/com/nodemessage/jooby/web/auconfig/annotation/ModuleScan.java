package com.nodemessage.jooby.web.auconfig.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author wjsmc
 * @date 2022/8/7 22:30
 * @description 模块扫描配置
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ModuleScan {
    String basePackage() default "";
}
