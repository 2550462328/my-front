package com.zhanghui.front.utils;

import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Class工具类
 *
 * @author: ZhangHui
 * @date: 2020/11/10 14:54
 * @version：1.0
 */
@Slf4j
public class ClassUtils {
    public static <T extends Annotation> boolean hasAnnotation(Class aClass, Class<T> annotation) {
        return aClass.getAnnotation(annotation) != null;
    }

    public static <T extends Annotation> Map<Method, T> getMethodMap(Class aClass, Class<T> annotation) {
        Method[] methods = aClass.getDeclaredMethods();
        HashMap<Method, T> map = new HashMap<>();
        for (Method method : methods) {
            T t = method.getAnnotation(annotation);
            if (t != null) {
                map.put(method, t);
            }
        }
        return map;
    }

    public static Class[] getSuperClasses(Class aClass) {
        ArrayList<Class> classes = new ArrayList<>();
        Class bClass = aClass.getSuperclass();
        while (bClass != null && !bClass.equals(Object.class)) {
            classes.add(bClass);
            Class[] classes2 = bClass.getInterfaces();
            for (Class aClass1 : classes2) {
                if (!classes.contains(aClass1)) {
                    classes.add(aClass1);
                }
            }
            bClass = bClass.getSuperclass();
        }
        Class[] classes1 = aClass.getInterfaces();
        for (Class aClass1 : classes1) {
            classes.add(aClass1);
        }
        return classes.toArray(new Class[classes.size()]);
    }

    public static ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Throwable localThrowable) {

        }

        if (cl == null) {
            cl = ClassUtils.class.getClassLoader();
            if (cl == null) {
                try {
                    cl = ClassLoader.getSystemClassLoader();
                } catch (Throwable localThrowable1) {
                    log.error("Cannot access system ClassLoader");
                }
            }
        }
        return cl;
    }

}
