package com.lanyu.jenkins.hellojenkins.common.exception;

import lombok.Data;

/**
 * @author lanyu
 * @date 2021年08月11日 11:07
 */
@Data
public class CaptchaException extends RuntimeException{
    private String msg;

    public CaptchaException(String msg) {
        super(msg);
        this.msg = msg;
    }
}
