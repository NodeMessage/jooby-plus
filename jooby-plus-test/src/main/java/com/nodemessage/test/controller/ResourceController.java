package com.nodemessage.test.controller;

import com.nodemessage.jooby.web.auconfig.annotation.Controller;
import io.jooby.Jooby;

/**
 * @author wjsmc
 * @date 2022/8/10 14:12
 * @description
 **/
@Controller("user")
public class ResourceController extends Jooby {
    {
        get("get", ctx -> {
            return "test";
        });
    }
}
