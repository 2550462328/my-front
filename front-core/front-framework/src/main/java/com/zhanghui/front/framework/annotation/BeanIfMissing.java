package com.zhanghui.front.framework.annotation;

import java.lang.annotation.*;

/**
 * @ClassName BeanIfMissing
 * @Description: 这是描述信息
 * @Author: ZhangHui
 * @Date: 2020/11/12
 * @Version：1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface BeanIfMissing {
    String value() default "";
}
