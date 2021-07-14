package com.zhuge.analysis.stat;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 通过注解采集一个事件
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ZhugeioTrackEvent {
    String eventName() default "";

    String properties() default "{}";
}
