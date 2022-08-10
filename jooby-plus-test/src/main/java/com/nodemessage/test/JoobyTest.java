package com.nodemessage.test;

import com.nodemessage.jooby.web.JoobyStarter;
import com.nodemessage.jooby.web.auconfig.annotation.ModuleScan;

@ModuleScan(basePackage = "com.nodemessage.test.JoobyTest")
public class JoobyTest {
    public static void main(String[] args) {
        JoobyStarter.run(args, JoobyTest.class);
    }
}
