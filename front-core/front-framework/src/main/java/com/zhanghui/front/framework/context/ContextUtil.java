package com.zhanghui.front.framework.context;

import java.util.List;
import java.util.Properties;

import com.zhanghui.front.framework.exception.InjectException;
import static com.zhanghui.front.framework.boot.FrontBootstrap.*;

/**
 * @author ZhangHui
 * @date 2020/11/12
 */
public class ContextUtil {
    private static ApplicationContext applicationContext;

    public static void setContext(ApplicationContext context) {
        applicationContext = context;
    }

    public static String getProperty(String key) {
        return applicationContext.getProperty(key);
    }

    public static Properties getProperty() {
        return applicationContext.getProperties();
    }

    public static Object getSource() {
        return applicationContext.getBeanByName(SOURCE_BEAN_NAME);
    }

    public static String[] getProperties(String key) {
        return applicationContext.getProperties(key);
    }

    public static Object getBean(String s) {
        return applicationContext.getBeanByName(s);
    }

    public static <T> T getBean(Class<T> tClass) {
        List<T> list = applicationContext.getBeans(tClass);
        if (list != null && list.size() > 1) {
            throw new InjectException("找不到明确的bean：含有多个同类型的bean可使用");
        }
        return (list == null || list.isEmpty()) ? null : list.get(0);
    }

    public static <T> List<T> getBeans(Class<T> tClass) {
        return applicationContext.getBeans(tClass);
    }
}
