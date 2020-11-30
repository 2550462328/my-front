package com.zhanghui.front.utils;

import com.zhanghui.front.framework.exception.InjectException;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: ZhangHui
 * @date: 2020/11/12 14:25
 * @version：1.0
 */
public class PropertyUtils {


    /**
     * 获取bean中被annotation注解的字段和set方法
     */
    public static <T extends Annotation> Map<Field, T> getProperties(Object bean, Class<T> annotation) {

        if (annotation == null || bean == null) {
            throw new InjectException("查找参数或属性值有误");
        }
        HashMap<Field, T> fieldTHashMap = new HashMap<>();

        // 处理bean类属性的set方法上面的annotation注解
        try {
            PropertyDescriptor[] pds = Introspector.getBeanInfo(bean.getClass()).getPropertyDescriptors();

            for (PropertyDescriptor propertyDescriptor : pds) {
                if (propertyDescriptor.getWriteMethod() != null) {

                    T t = propertyDescriptor.getWriteMethod().getAnnotation(annotation);

                    if (t == null) {
                        continue;
                    }
                    fieldTHashMap.put(bean.getClass().getDeclaredField(propertyDescriptor.getName()), t);
                }
            }

        } catch (IntrospectionException | NoSuchFieldException e) {
            throw new InjectException("查找 " + bean.getClass().getName() + " 的属性时发生错误", e.getCause());
        }

        // 处理bean类的字段上面的annotation注解
        Field[] fields = bean.getClass().getDeclaredFields();

        for (Field field : fields) {
            T t = field.getAnnotation(annotation);
            if (t != null) {
                fieldTHashMap.put(field, t);
            }
        }
        return fieldTHashMap;
    }

    public static void setProperty(Object bean, Field field, Object value) {

        if (field == null || value == null || bean == null) {
            throw new InjectException("注入参数或属性值有误");
        }
        try {
            PropertyDescriptor[] pds = Introspector.getBeanInfo(bean.getClass()).getPropertyDescriptors();

            PropertyDescriptor targetFieldPd = null;

            for (PropertyDescriptor pd : pds) {
                if (StringUtils.equals(pd.getName(), field.getName())) {
                    targetFieldPd = pd;
                }
                break;
            }
            if (targetFieldPd != null && targetFieldPd.getWriteMethod() != null) {
                Method method = targetFieldPd.getWriteMethod();
                if (!method.isAccessible()) {
                    method.setAccessible(true);
                }
                method.invoke(bean, value);
            } else {
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                field.set(bean, value);
            }
        } catch (Exception e) {
            throw new InjectException("注入 " + bean.getClass().getName() + " 时发生错误", e.getCause());
        }
    }
}
