package com.nodemessage.jooby.web;


import com.google.inject.Guice;
import com.google.inject.Injector;
import com.nodemessage.jooby.web.auconfig.ComponentInject;
import com.nodemessage.jooby.web.auconfig.JoobyStarterStore;
import com.nodemessage.jooby.web.auconfig.ModuleInject;
import com.nodemessage.jooby.web.auconfig.ModuleScanCof;

/**
 * @author wjsmc
 * @date 2022/8/7 22:35
 * @description
 **/

public class JoobyStarter {

    public static Injector moduleInjector = Guice.createInjector(new ModuleInject());
    public static Injector componentInjector;

    public static void run(String[] args, Class<?> rootClass) {
        ModuleScanCof moduleScanCof = moduleInjector.getInstance(ModuleScanCof.class);
        JoobyStarterStore starterStore = moduleInjector.getInstance(JoobyStarterStore.class);
        starterStore.setBasePackage(rootClass.getPackage().getName());
        moduleScanCof.scan();
        componentInjector = Guice.createInjector(new ComponentInject());
        JoobyApplication.runApp(args, JoobyApplication::new);
    }
}
