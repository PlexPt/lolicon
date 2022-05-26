package com.github.plexpt.lolicon.lolicon.aop;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author : wh
 * @date : 2020/7/24 10:44
 * 记录方法执行时间注解
 * 自己调用自己请使用 通过暴露代理类方式调用
 * ((ClearServiceImpl)AopContext.currentProxy()).testAOP1();
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TimeLog {

    /**
     * 方法描述
     *
     * @return
     */
    String methodDesc() default "";
}
