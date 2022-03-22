package com.he.annotation;


import java.lang.annotation.*;

//type添加到类上，method添加到方法上
//@Target({ElementType.TYPE, ElementType.METHOD})
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogAnnotation {
    String module()  default  "";
    String operater() default   "";
}
