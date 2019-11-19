package com.jiang.medical.util;

import java.lang.annotation.*;

/**
 * @author Goofy
 * Excel注解，用以生成Excel表格文件
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Excel {
     
    //列名
    String name() default "";
     
    String orderBy();
    
    boolean display() default true;
     
}