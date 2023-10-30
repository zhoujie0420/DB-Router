package org.jiezhou.annotation;


import java.lang.annotation.*;


/**
 * 分表注解
 */
@Documented
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DBRouterStrategy {

    boolean splitTable() default false;

}
