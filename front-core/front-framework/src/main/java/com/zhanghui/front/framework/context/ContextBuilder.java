package com.zhanghui.front.framework.context;

import com.zhanghui.front.framework.annotation.Bean;
import com.zhanghui.front.framework.annotation.BeanIfMissing;
import com.zhanghui.front.framework.annotation.Config;
import com.zhanghui.front.framework.context.inject.Injector;
import com.zhanghui.front.framework.exception.InitException;
import com.zhanghui.front.framework.executor.handler.Handler;
import com.zhanghui.front.framework.parsing.DefaultPackageScanner;
import com.zhanghui.front.framework.parsing.DefaultPropertyResolver;
import com.zhanghui.front.framework.parsing.PackageScanner;
import com.zhanghui.front.framework.parsing.PropertyResolver;
import com.zhanghui.front.utils.ClassUtils;
import com.zhanghui.front.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.zhanghui.front.framework.boot.FrontBootstrap.*;


/**
 * 核心类
 * @author: ZhangHui
 * @date: 2020/11/10 14:43
 * @version：1.0
 */
@Slf4j
public class ContextBuilder {

    private final Properties properties;
    private ApplicationContext applicationContext;

    // 用于应对注入数组的情况 比如 @Autowired List<Student> students;
    private Map<Class, List> typeMap;

    // 对容器中所有bean的存储
    private Map<String, Object> beanMap;

    // 注入工具类
    private List<Injector> injectors;

    // 存放所有被@BeanIfMissing注解的Bean
    private List<Class> missBeans;

    // client端的信息 启动类的信息
    private final Object source;

    public ContextBuilder(Object source, Properties properties) {
        this.properties = properties;
        this.typeMap = new ConcurrentHashMap<>();
        this.beanMap = new ConcurrentHashMap<>();
        this.applicationContext = new ApplicationContext();
        this.source = source;
        this.injectors = new ArrayList<>();
        this.missBeans = new ArrayList<>();

        preResource(source, properties);
    }

    private void preResource(Object source, Properties properties) {
        PropertyResolver propertyResolver = new DefaultPropertyResolver();

        if (StringUtils.isNotBlank(properties.getProperty(CONFIG_LOCATIONS))) {
            String paths = properties.getProperty(CONFIG_LOCATIONS);
            String[] pathArr = paths.split(",");
            for (String path : pathArr) {
                propertyResolver.resolve(properties, path);
            }
        } else {
            propertyResolver.resolve(properties, properties.getProperty(CONFIG_LOCATION));
        }

        this.beanMap.put(SOURCE_BEAN_NAME,source);
        this.beanMap.put(StringUtils.toLowerCaseFirstOne(source.getClass().getSimpleName()), source);

        this.applicationContext.setProperties(this.properties);
        this.applicationContext.setBeanMap(this.beanMap);
        this.applicationContext.setTypeMap(this.typeMap);

        ContextUtil.setContext(this.applicationContext);
    }

    public ApplicationContext builder() {
        List<Class> classesContainer = scanForClasses();
        doBuild(classesContainer);
        doInject();
        this.applicationContext.init();
        return applicationContext;
    }

    private List<Class> scanForClasses() {
        List<Class> classesContainer = new ArrayList<>();

        PackageScanner packageScaner = new DefaultPackageScanner();

        String packageLocation = properties.getProperty(PACKAGE_SCAN_LOCATION);

        packageScaner.doScan(classesContainer, source.getClass().getPackage().getName());
        packageScaner.doScan(classesContainer, CURRENT_PACKAGE_LOCATION);

        if (StringUtils.isNotBlank(packageLocation)) {
            String[] packageArr = packageLocation.split(",");
            for (String location : packageArr) {
                packageScaner.doScan(classesContainer, location);
            }
        }

        return classesContainer;
    }

    private void doBuild(List<Class> classes) {

        try {
            for (Class inClass : classes) {
                //排除接口和抽象类
                if (inClass.isInterface() || Modifier.isAbstract(inClass.getModifiers())) {
                    continue;
                }

                // 处理Handller的子类
                if (Handler.class.isAssignableFrom(inClass)) {
                    Object o = inClass.newInstance();
                    putObject(o);
                    continue;
                }

                // 处理Injector的子类
                if (Injector.class.isAssignableFrom(inClass)) {
                    Object o = inClass.newInstance();
                    injectors.add((Injector) o);
                    continue;
                }

                // 处理被@Bean注释的类
                if (ClassUtils.hasAnnotation(inClass, Bean.class)) {
                    Bean bean = (Bean) inClass.getAnnotation(Bean.class);
                    Object o = inClass.newInstance();
                    if (StringUtils.isNotBlank(bean.value())) {
                        putObject(bean.value(), o, ClassUtils.getSuperClasses(inClass));
                    } else {
                        putObject(o);
                    }
                    continue;
                }

                // 处理被@Config注释的类
                if (ClassUtils.hasAnnotation(inClass, Config.class)) {
                    Object o = inClass.newInstance();
                    putObject(o);
                    for (Injector injector : injectors) {
                        injector.inject(o);
                    }
                    Map<Method, Bean> methodBeanMap = ClassUtils.getMethodMap(inClass, Bean.class);
                    for (Map.Entry<Method, Bean> entry : methodBeanMap.entrySet()) {

                        Method method = entry.getKey();
                        Bean bean = entry.getValue();

                        Object methodBean = method.invoke(o);

                        if (methodBean == null) {
                            throw new InitException("方法：" + method.getName() + "返回了null");
                        }

                        if (StringUtils.isNotBlank(bean.value())) {
                            putObject(bean.value(), methodBean, ClassUtils.getSuperClasses(method.getReturnType()));
                        } else {
                            putObject(method.getName(), methodBean, ClassUtils.getSuperClasses(method.getReturnType()));
                        }
                        continue;
                    }
                }

                // 处理被@BeanIfMissing注释的类
                if (ClassUtils.hasAnnotation(inClass, BeanIfMissing.class)) {
                    this.missBeans.add(inClass);
                    continue;
                }

                for (Class missBean : missBeans) {
                    BeanIfMissing beanIfMissing = (BeanIfMissing) missBean.getAnnotation(BeanIfMissing.class);
                    if (StringUtils.isNotBlank(beanIfMissing.value())) {
                        putMissObject(missBean, beanIfMissing.value(), ClassUtils.getSuperClasses(missBean));
                    } else {
                        putMissObject(missBean, missBean.getSimpleName(), ClassUtils.getSuperClasses(missBean));
                    }
                }
            }
        } catch (Throwable e) {
            log.error("出现系统异常：[{}]", e.getMessage(), e.getCause());
        }
    }

    private void putObject(Object o) {
        putObject(o.getClass(), o);
    }

    private void putObject(Class aClass, Object o) {
        String name = StringUtils.toLowerCaseFirstOne(aClass.getSimpleName());
        putObject(name, o, ClassUtils.getSuperClasses(aClass));
    }

    private void putObject(String name, Object o, Class... superClasses) {
        List<Object> list1 = new ArrayList<>();
        list1.add(o);
        this.typeMap.put(o.getClass(), list1);
        for (Class superClass : superClasses) {
            if (this.typeMap.containsKey(superClass)) {
                List list = this.typeMap.get(superClass);
                list.add(o);
            } else {
                List list = new ArrayList();
                list.add(o);
                this.typeMap.put(superClass, list);
            }
        }

        if (this.beanMap.containsKey(name)) {
            throw new InitException("有两个或多个名称一样的bean：" + name);
        }
        this.beanMap.put(name, o);
    }

    /**
     * 如果当前容器中没有aClass实例才放进去，同理它的superClassed也是
     */
    private void putMissObject(Class aClass, String name, Class... superClasses) throws Exception {

        int addCount = 0;
        Object o = null;

        for (Class superClass : superClasses) {
            if (this.typeMap.containsKey(superClass)) {
                continue;
            } else {
                if (o == null) {
                    o = aClass.newInstance();
                }
                List list = new ArrayList();
                list.add(o);
                this.typeMap.put(superClass, list);
                addCount++;
            }
        }

        if (addCount > 0 && o != null) {
            if (this.typeMap.containsKey(aClass)) {
                throw new InitException("添加Bean实例失败：" + name);
            } else {
                List list = new ArrayList();
                list.add(o);
                this.typeMap.put(aClass, list);
            }
            addCount++;
        }

        if (superClasses.length == 0 && !this.typeMap.containsKey(aClass)) {
            o = aClass.newInstance();
            List list = new ArrayList();
            list.add(o);
            this.typeMap.put(aClass, list);
            addCount++;
        }
        if (addCount > 0 && o != null) {
            name = StringUtils.toLowerCaseFirstOne(name);
            if(this.beanMap.containsKey(name)){
                throw new InitException("有两个或多个名称一样的bean：" + name);
            }
            this.beanMap.put(name,o);
        }
    }

    private void doInject() {

        for(Object bean : beanMap.values()){
            for(Injector injector : injectors){
                injector.inject(bean);
            }
        }
    }
}
