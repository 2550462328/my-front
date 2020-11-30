package com.zhanghui.front.framework.context.inject;

import com.zhanghui.front.framework.annotation.Need;
import com.zhanghui.front.framework.context.ContextUtil;
import com.zhanghui.front.framework.exception.InjectException;
import com.zhanghui.front.utils.PropertyUtils;
import com.zhanghui.front.utils.StringUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * @author: ZhangHui
 * @date: 2020/11/12 14:15
 * @version：1.0
 */
public class BeanInjector implements Injector {

    @Override
    public void inject(Object o) {
        Map<Field, Need> fieldNeedMap = PropertyUtils.getProperties(o,Need.class);

        // 对o中被@Need注解的字段和set+属性方法进行注入
        for(Map.Entry<Field,Need> entry : fieldNeedMap.entrySet()){

            Field field = entry.getKey();
            Need needAnnotation = entry.getValue();
            Object target = null;

            if(StringUtils.isNotBlank(needAnnotation.value())){
                target = ContextUtil.getBean(needAnnotation.value());
            }else{
                if(field.getType().isArray()){
                    List list = ContextUtil.getBeans(field.getType().getComponentType());
                    if(list != null){
                        target = list.toArray((Object[]) Array.newInstance(field.getType().getComponentType(),list.size()));
                    }
                }else{
                    target = ContextUtil.getBean(field.getType());
                }
            }

            if (target == null && needAnnotation.require()) {
                throw new InjectException(o.getClass().getName() + " : " + field.getName() + "找不到对应的bean");
            }

            if (!field.getType().isAssignableFrom(target.getClass()) && !field.getType().isArray()) {
                throw new InjectException(o.getClass().getName() + " : " + field.getName() + "对应的bean不是该类型");
            }

            if(target != null){
                PropertyUtils.setProperty(o, field, target);
            }
        }

    }
}
