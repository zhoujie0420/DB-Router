package org.jiezhou.annotation;


import java.lang.annotation.*;

@Documented //文档注解
@Retention(RetentionPolicy.RUNTIME) //运行时注解 ,生命周期
@Target({ElementType.TYPE,ElementType.METHOD})
public @interface DBRouter {

    /**
     * 分库分表字段
     * @return
     */
    String key() default "";
}
