package com.nodemessage.jooby.mybatis;

import lombok.Data;

/**
 * @author wjsmc
 * @date 2022/8/10 20:58
 * @description
 **/
@Data
public class ConfigEntity {
    private String url;
    private String driver;
    private String username;
    private String password;
    private String mapperPath;
    private String packageName;
}
