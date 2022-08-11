package com.nodemessage.jooby.web.config;

import io.jooby.Jooby;

/**
 * @author wjsmc
 * @date 2022/8/10 14:49
 * @description
 **/
public interface WebConfig {

    void defaultConfig(Jooby jooby);

    default JoobyConfig fastSetting(Jooby jooby) {
        return new JoobyConfig(jooby);
    }

}
