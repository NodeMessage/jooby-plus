package com.nodemessage.jooby.web;


import com.nodemessage.jooby.web.auconfig.ControllerScanCof;
import io.jooby.Jooby;
import io.jooby.di.GuiceModule;


/**
 * @author wjsmc
 * @date 2022/8/7 23:10
 * @description
 **/
public class JoobyApplication extends Jooby {

    public static ControllerScanCof controllerScanCof;

    {
        install(new GuiceModule());
        setController();
    }

    public void setController() {
        controllerScanCof.register(this);
    }
}
