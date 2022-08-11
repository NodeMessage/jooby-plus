package com.nodemessage.test.controller;

import com.nodemessage.jooby.web.auconfig.annotation.Component;
import com.nodemessage.jooby.web.config.WebConfig;
import io.jooby.Jooby;

/**
 * @author wjsmc
 * @date 2022/8/10 15:01
 * @description
 **/
@Component
public class Config implements WebConfig {
    @Override
    public void defaultConfig(Jooby jooby) {

    }
}
