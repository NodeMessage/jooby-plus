package com.nodemessage.jooby.web.auconfig.impl;


import com.nodemessage.jooby.web.JoobyApplication;
import com.nodemessage.jooby.web.auconfig.ComponentInject;
import com.nodemessage.jooby.web.auconfig.ControllerScanCof;
import com.nodemessage.jooby.web.auconfig.JoobyStarterStore;
import com.nodemessage.jooby.web.auconfig.ModuleScanCof;
import com.nodemessage.jooby.web.auconfig.annotation.Component;
import com.nodemessage.jooby.web.auconfig.annotation.Controller;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author wjsmc
 * @date 2022/8/8 13:36
 * @description 模块扫描
 **/
@Singleton
public class IModuleScanCof implements ModuleScanCof {

    @Inject
    private JoobyStarterStore joobyStarterStore;
    @Inject
    private ControllerScanCof controllerScanCof;

    @Override
    public void scan(Class<?> rootClass) {

        URL resource = Thread.currentThread().getContextClassLoader().getResource("./");
        if (resource != null && "file".equals(resource.getProtocol())) {
            try {
                String basePackage = joobyStarterStore.getBasePackage();
                basePackage = basePackage.replace(".", File.separator);
                resource = Thread.currentThread().getContextClassLoader().getResource(basePackage);
                Path path = Paths.get((resource.toURI()));
                String finalBasePackage = basePackage;
                Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        Class<?> clazz = getComponentClass(file, finalBasePackage, path);
                        if (clazz == null) {
                            return FileVisitResult.CONTINUE;
                        }
                        getComponent(clazz);
                        return FileVisitResult.CONTINUE;
                    }
                });
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        } else {
            ArrayList<String> classJarPaths = getClassJarPaths(rootClass);
            for (String classJarPath : classJarPaths) {
                Class<?> clazz = getComponentClass(classJarPath);
                if (clazz == null) {
                    continue;
                }
                getComponent(clazz);
            }
        }
        JoobyApplication.controllerScanCof = controllerScanCof;
    }

    private void getComponent(Class<?> clazz) {
        Type[] genericSuperclass;
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
        if (clazz.isAnnotationPresent(Controller.class)) {
            controllerScanCof.addResourceControllers(clazz);
        }
    }

    private Class<?> getComponentClass(String classPath) {
        try {
            Class<?> aClass = Class.forName(classPath.replace("/", ".").replace(".class", ""));
            if (aClass.isAnnotationPresent(Component.class) ||
                    aClass.isAnnotationPresent(Controller.class) ||
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

    private Class<?> getComponentClass(Path classPath, String basePackage, Path basePath) {
        String classPackage = (basePackage + (classPath.toString().replace(basePath.toString(), "")))
                .replace(File.separator, ".").replace(".class", "");
        try {
            Class<?> aClass = Class.forName(classPackage);
            if (aClass.isAnnotationPresent(Component.class) ||
                    aClass.isAnnotationPresent(Controller.class) ||
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

    public ArrayList<String> getClassJarPaths(Class<?> rootClass) {
        ArrayList<String> classJarPath = new ArrayList<>();
        String basePackage = joobyStarterStore.getBasePackage();
        basePackage = basePackage.replace(".", "/");
        try (JarFile jarFile = new JarFile(rootClass.getProtectionDomain().getCodeSource().getLocation().getPath())) {
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                String name = entries.nextElement().getName();
                if (name.startsWith(basePackage) && name.endsWith(".class") && !name.contains("$")) {
                    classJarPath.add(name);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return classJarPath;
    }
}
