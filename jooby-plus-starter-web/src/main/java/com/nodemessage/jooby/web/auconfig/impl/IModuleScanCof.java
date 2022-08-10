package com.nodemessage.jooby.web.auconfig.impl;


import com.nodemessage.jooby.web.JoobyApplication;
import com.nodemessage.jooby.web.JoobyStarter;
import com.nodemessage.jooby.web.auconfig.ComponentInject;
import com.nodemessage.jooby.web.auconfig.ControllerScanCof;
import com.nodemessage.jooby.web.auconfig.JoobyStarterStore;
import com.nodemessage.jooby.web.auconfig.ModuleScanCof;
import com.nodemessage.jooby.web.auconfig.annotation.Component;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * @author wjsmc
 * @date 2022/8/8 13:36
 * @description
 **/
@Singleton
public class IModuleScanCof implements ModuleScanCof {

    @Inject
    private JoobyStarterStore joobyStarterStore;
    @Inject
    private ControllerScanCof controllerScanCof;

    @Override
    public void scan() {
        URL resource = JoobyStarter.class.getClassLoader().getResource("");
        String basePackage = joobyStarterStore.getBasePackage();
        basePackage = basePackage.replace(".", File.separator);
        Path path = Paths.get((resource.getPath() + basePackage).substring(1));
        try {
            String finalBasePackage = basePackage;
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Class<?> clazz = getComponentClass(file, finalBasePackage, path);
                    Type[] genericSuperclass;
                    if (clazz == null) {
                        return FileVisitResult.CONTINUE;
                    }
                    if ((genericSuperclass = clazz.getGenericInterfaces()).length != 0) {
                        ComponentInject.beans.put((Class) genericSuperclass[0], clazz);
                    } else {
                        try {
                            ComponentInject.beansInstant.put(clazz,
                                    clazz.newInstance());
                        } catch (InstantiationException | IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                    if (clazz.isAnnotationPresent(io.jooby.annotations.Path.class)) {
                        controllerScanCof.addControllers(clazz);
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
            JoobyApplication.controllerScanCof = controllerScanCof;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Class<?> getComponentClass(Path classPath, String basePackage, Path basePath) {
        String classPackage = (basePackage + (classPath.toString().replace(basePath.toString(), "")))
                .replace(File.separator, ".").replace(".class", "");
        try {
            Class<?> aClass = Class.forName(classPackage);
            if (aClass.isAnnotationPresent(Component.class) ||
                    aClass.isAnnotationPresent(io.jooby.annotations.Path.class)) {
                return aClass;
            } else {
                return null;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
