package com.nodemessage.jooby.mybatis;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * @author wjsmc
 * @date 2022/8/10 21:00
 * @description
 **/
public class MybatisConfig {

    public static ConfigEntity configEntity;

    static {
        configEntity = new ConfigEntity();
        Config application = ConfigFactory.load("application");
        configEntity.setUrl(application.getString("mybatis.url"));
        configEntity.setDriver(application.getString("mybatis.driver"));
        configEntity.setUsername(application.getString("mybatis.username"));
        configEntity.setPassword(application.getString("mybatis.password"));
        configEntity.setMapperPath(application.getString("mybatis.mapper-path"));
        configEntity.setPackageName(application.getString("mybatis.package-name"));
    }
}
