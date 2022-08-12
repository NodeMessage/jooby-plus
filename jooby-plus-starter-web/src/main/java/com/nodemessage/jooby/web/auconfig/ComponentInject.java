package com.nodemessage.jooby.web.auconfig;


import com.google.inject.AbstractModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

/**
 * @author wjsmc
 * @date 2022/8/8 18:07
 * @description 组件自动注册
 **/
public class ComponentInject extends AbstractModule {

    Logger logger = LoggerFactory.getLogger(ComponentInject.class);

    public static HashMap<Class, Class> beans = new HashMap<>();
    public static HashMap<Class, Object> beansInstant = new HashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    protected void configure() {
        beans.forEach((k, v) -> {
            bind(k).to(v);
        });
        beansInstant.forEach((k, v) -> {
            bind(k).toInstance(v);
        });
        logger.info("load component successful.");
    }
}
