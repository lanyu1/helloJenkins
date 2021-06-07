package com.lanyu.jenkins.hellojenkins.common.annotation;

import com.lanyu.jenkins.hellojenkins.common.enums.LogType;

import java.lang.annotation.*;

/**
 * 系统日志自定义朱姐
 * @author lanyu
 * @date 2021年06月07日 17:15
 */
@Target({ElementType.PARAMETER,ElementType.METHOD})//作用于参数或方法上
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SystemLog {

    String description() default "";

    LogType type() default LogType.OPERATION;
}
