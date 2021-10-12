package com.hhr.jf.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: Harry
 * @Date: 2021/10/12 8:07
 * @Version 1.0
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface JfComponent {
    boolean weakReference() default false;
}
