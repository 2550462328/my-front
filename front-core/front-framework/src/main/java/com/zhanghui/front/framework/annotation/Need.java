package com.zhanghui.front.framework.annotation;

import java.lang.annotation.*;

/**
 * @author: ZhangHui
 * @date: 2020/11/12 14:16
 * @version：1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface Need {

    String value() default "";

    boolean require() default true;
}