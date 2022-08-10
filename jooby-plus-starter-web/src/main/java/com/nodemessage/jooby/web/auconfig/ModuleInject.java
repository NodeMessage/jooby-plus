package com.nodemessage.jooby.web.auconfig;


import com.google.inject.AbstractModule;
import com.nodemessage.jooby.web.auconfig.impl.IControllerScanCof;
import com.nodemessage.jooby.web.auconfig.impl.IJoobyStarterStore;
import com.nodemessage.jooby.web.auconfig.impl.IModuleScanCof;

/**
 * @author wjsmc
 * @date 2022/8/8 0:02
 * @description
 **/
public class ModuleInject extends AbstractModule {

    @Override
    protected void configure() {
        bind(JoobyStarterStore.class).to(IJoobyStarterStore.class);
        bind(ModuleScanCof.class).to(IModuleScanCof.class);
        bind(ControllerScanCof.class).to(IControllerScanCof.class);
    }

}
