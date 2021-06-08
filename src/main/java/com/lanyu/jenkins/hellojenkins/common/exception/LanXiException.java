package com.lanyu.jenkins.hellojenkins.common.exception;

/**
 * 异常类
 * @author lanyu
 * @date 2021年06月08日 13:35
 */
public class LanXiException extends RuntimeException{

    private String msg;

    public LanXiException(String msg){
        super(msg);
        this.msg = msg;
    }
}
