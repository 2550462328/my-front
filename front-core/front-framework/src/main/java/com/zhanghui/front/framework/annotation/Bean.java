package com.zhanghui.front.framework.annotation;

import java.lang.annotation.*;

/**
 * @ClassName Bean
 * @Description: 这是描述信息
 * @Author: ZhangHui
 * @Date: 2020/11/11
 * @Version：1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface Bean {
    String value() default "";
}
