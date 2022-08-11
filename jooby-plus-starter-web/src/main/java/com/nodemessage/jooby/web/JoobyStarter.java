package com.nodemessage.jooby.web;


import com.google.inject.Guice;
import com.google.inject.Injector;
import com.nodemessage.jooby.mybatis.JoobyMybatisPlus;
import com.nodemessage.jooby.web.auconfig.ComponentInject;
import com.nodemessage.jooby.web.auconfig.JoobyStarterStore;
import com.nodemessage.jooby.web.auconfig.ModuleInject;
import com.nodemessage.jooby.web.auconfig.ModuleScanCof;
import com.nodemessage.jooby.web.auconfig.annotation.JoobyPlusStarter;
import com.nodemessage.jooby.web.config.WebConfig;
import io.jooby.Jooby;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

/**
 * @author wjsmc
 * @date 2022/8/7 22:35
 * @description Jooby Web 自动启动器
 **/
public class JoobyStarter {

    private static Logger logger = LoggerFactory.getLogger(JoobyStarter.class);
    public static Injector moduleInjector = Guice.createInjector(new ModuleInject());
    public static Injector componentInjector;
    public static JoobyApplication joobyApplication;

    /**
     * @return void
     * @description 自动扫描器，负责组件注册与web容器自动启动
     * @date 2022/8/11 13:43
     * @author wjsmc
     **/
    public static void run(String[] args, Class<?> rootClass) {

        /* 模块扫描 */
        ModuleScanCof moduleScanCof = moduleInjector.getInstance(ModuleScanCof.class);
        JoobyStarterStore starterStore = moduleInjector.getInstance(JoobyStarterStore.class);
        starterStore.setBasePackage(rootClass.getPackage().getName());
        moduleScanCof.scan(rootClass);

        /* 组件扫描 */
        componentInjector = Guice.createInjector(new ComponentInject());

        /* 自定义配置 */
        try {
            joobyApplication = new JoobyApplication(componentInjector.getInstance(WebConfig.class));
        } catch (Exception e) {
            logger.info("未配置自定义信息");
            joobyApplication = new JoobyApplication();
        }

        /* 运行容器 */
        JoobyApplication.runApp(args,
                rootClass.getAnnotation(JoobyPlusStarter.class).EXECUTION_MODE()
                , new Supplier<Jooby>() {
                    @Override
                    public Jooby get() {
                        return joobyApplication;
                    }
                });

        new Banner().setting();
        logger.info("Jooby-Plus Started Successful.");
        try {
            byte init = JoobyMybatisPlus.init;
            logger.info("Jooby-Plus-MybatisPlus Started Successful.");
        } catch (Error ignored) {

        }
    }

}
