package com.nodemessage.jooby.web;


import com.nodemessage.jooby.web.auconfig.ControllerScanCof;
import com.nodemessage.jooby.web.config.WebConfig;
import io.jooby.Jooby;
import io.jooby.di.GuiceModule;


/**
 * @author wjsmc
 * @date 2022/8/7 23:10
 * @description Jooby web容器
 **/
public class JoobyApplication extends Jooby {

    public static ControllerScanCof controllerScanCof;

    public JoobyApplication() {
        run();
    }

    /**
     * @description 注册自定义配置
     * @date 2022/8/11 13:47
     * @author wjsmc
     **/
    public JoobyApplication(WebConfig webConfig) {
        if (webConfig != null) {
            webConfig.defaultConfig(this);
        }
        run();
    }

    private void run() {
        install(new GuiceModule());
        setController();
    }

    /**
     * @description 注册 mcv 控制器
     * @date 2022/8/11 13:45
     * @author wjsmc
     **/
    public void setController() {
        controllerScanCof.register(this);
    }

}
