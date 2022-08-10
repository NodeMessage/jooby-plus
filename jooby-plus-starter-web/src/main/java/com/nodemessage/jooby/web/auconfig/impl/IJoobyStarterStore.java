package com.nodemessage.jooby.web.auconfig.impl;


import com.nodemessage.jooby.web.auconfig.JoobyStarterStore;

import javax.inject.Singleton;

/**
 * @author wjsmc
 * @date 2022/8/7 22:58
 * @description
 **/
@Singleton
public class IJoobyStarterStore implements JoobyStarterStore {
    private String basePackage;

    @Override
    public String getBasePackage() {
        return basePackage;
    }

    @Override
    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }
}
