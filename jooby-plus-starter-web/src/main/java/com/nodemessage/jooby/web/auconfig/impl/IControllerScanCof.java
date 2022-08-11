package com.nodemessage.jooby.web.auconfig.impl;


import com.nodemessage.jooby.web.JoobyStarter;
import com.nodemessage.jooby.web.auconfig.ControllerScanCof;
import com.nodemessage.jooby.web.auconfig.annotation.Controller;
import io.jooby.Jooby;
import io.jooby.Router;

import javax.inject.Singleton;
import java.util.LinkedList;

/**
 * @author wjsmc
 * @date 2022/8/8 21:17
 * @description
 **/
@Singleton
public class IControllerScanCof implements ControllerScanCof {

    private LinkedList<Class<?>> controllers = new LinkedList<>();
    private LinkedList<Class<?>> resourceControllers = new LinkedList<>();

    @Override
    public void register(Jooby jooby) {
        controllers.forEach(e -> jooby.mvc(JoobyStarter.componentInjector.getInstance(e)));
        resourceControllers.forEach(e -> jooby.mount(e.getAnnotation(Controller.class).value(),
                (Router) JoobyStarter.componentInjector.getInstance(e)));
    }

    @Override
    public void addControllers(Class<?> clazz) {
        this.controllers.add(clazz);
    }

    @Override
    public void addResourceControllers(Class<?> clazz) {
        resourceControllers.add(clazz);
    }
}
