package com.zhanghui.front.framework.context;

import com.zhanghui.front.framework.executor.connector.Connector;

import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 核心类
 * @author: ZhangHui
 * @date: 2020/11/10 14:46
 * @version：1.0
 */
public class ApplicationContext implements Container {

    private Properties properties;

    private Map<String, Object> beanMap;

    private Map<Class, List> typeMap;

    private String status = "null";

    @Override
    public void init() {
        if ("null".equals(status)) {
            status = "init";
        } else {
            return;
        }
        for (Object o : this.beanMap.values()) {
            if (o instanceof Container) {
                ((Container) o).init();
            }
        }
    }

    @Override
    public void start() {
        if ("init".equals(status)) {
            status = "start";
        } else {
            return;
        }
        for (Object o : this.beanMap.values()) {
            if (o instanceof Container && !(o instanceof Connector)) {
                ((Container) o).start();
            }
        }
        for (Object o : this.beanMap.values()) {
            if (o instanceof Connector) {
                ((Connector) o).start();
            }
        }
    }

    @Override
    public void stop() {
        if ("stop".equals(status)) {
            return;
        } else {
            status = "stop";
        }
        for (Object o : this.beanMap.values()) {
            if (o instanceof Container) {
                ((Container) o).stop();
            }
        }
        try {
            for (Object o : this.beanMap.values()) {
                if (o instanceof Connector) {
                    ((Connector) o).destroy();
                }
            }
        } catch (Exception e) {
            // ignore Exception
        }
        this.beanMap.clear();
    }

    String getProperty(String key) {
        return this.properties.getProperty(key);
    }

    String[] getProperties(String key) {
        String s = this.properties.getProperty(key);
        if (s == null) {
            return null;
        }
        return s.split(",");
    }

    Properties getProperties() {
        return properties;
    }

    public void setBeanMap(Map<String, Object> beanMap) {
        this.beanMap = beanMap;
    }

    void setProperties(Properties properties) {
        this.properties = properties;
    }

    void setTypeMap(Map<Class, List> typeMap) {
        this.typeMap = typeMap;
    }

    Object getBeanByName(String name) {
        return beanMap.get(name);
    }

    <T> List<T> getBeans(Class<T> tClass) {
        return this.typeMap.get(tClass);
    }
}
