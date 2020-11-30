package com.zhanghui.front.framework.annotation;

import java.lang.annotation.*;

/**
 * @author: ZhangHui
 * @date: 2020/11/13 10:45
 * @version：1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface Value {
    String value() default "";

    boolean require() default true;
}
