package com.zhanghui.front.framework.context.inject;

import com.zhanghui.front.framework.annotation.Value;
import com.zhanghui.front.framework.context.ContextUtil;
import com.zhanghui.front.framework.exception.InjectException;
import com.zhanghui.front.utils.PropertyUtils;
import com.zhanghui.front.utils.StringUtils;

import java.lang.reflect.Field;

/**
 * @author: ZhangHui
 * @date: 2020/11/13 15:10
 * @version：1.0
 */
public class ValueInjector implements Injector {


    @Override
    public void inject(Object o) {

        Field[] fields = o.getClass().getDeclaredFields();

        for (Field field : fields) {

            if (field.isAnnotationPresent(Value.class)) {
                Value valueAnnotation = field.getAnnotation(Value.class);
                String value;

                if (StringUtils.isNotBlank(valueAnnotation.value())) {
                    value = ContextUtil.getProperty(valueAnnotation.value());
                } else {
                    value = ContextUtil.getProperty(field.getName());
                }

                if (StringUtils.isBlank(value)) {
                    throw new InjectException(String.format("找不到[%s]对应的值", field.getName()));
                }
                Object convertObj = convert(field.getType(), value);
                PropertyUtils.setProperty(o, field, convertObj);
            }
        }
    }

    private Object convert(Class aClass, String s) {
        switch (aClass.getSimpleName()) {
            case "int":
            case "Integer":
                return Integer.valueOf(s);
            case "long":
            case "Long":
                return Long.valueOf(s);
            case "int[]":
            case "Integer[]":
                String[] ts = s.split(",");
                int[] is = new int[ts.length];
                for (int i = ts.length - 1; i >= 0; i--) {
                    is[i] = Integer.valueOf(ts[i]);
                }
                return is;
            case "long[]":
            case "Long[]":
                String[] tsl = s.split(",");
                long[] isl = new long[tsl.length];
                for (int i = tsl.length - 1; i >= 0; i--) {
                    isl[i] = Long.valueOf(tsl[i]);
                }
                return isl;
            case "float":
            case "Float":
                return Float.valueOf(s);
            case "double":
            case "Double":
                return Double.valueOf(s);
            case "String":
                return s;
            case "String[]":
                return s.split(",");
            default:
                throw new InjectException("不能转换为对应类型:" + aClass.getSimpleName());
        }

    }
}
