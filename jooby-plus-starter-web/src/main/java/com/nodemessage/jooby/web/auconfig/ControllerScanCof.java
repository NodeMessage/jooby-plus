package com.nodemessage.jooby.web.auconfig;

import io.jooby.Jooby;

/**
 * @author wjsmc
 * @date 2022/8/8 21:17
 * @description
 **/
public interface ControllerScanCof {
    void register(Jooby jooby);
    void addControllers(Class<?> clazz) ;
    void addResourceControllers(Class<?> clazz) ;
}
