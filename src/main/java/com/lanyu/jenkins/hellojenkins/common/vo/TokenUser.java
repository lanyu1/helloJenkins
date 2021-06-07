package com.lanyu.jenkins.hellojenkins.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * token用户实体类
 * @author lanyu
 * @date 2021年06月07日 17:15
 */
@Data
@AllArgsConstructor
public class TokenUser {

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户权限
     */
    private List<String> permissions;

    /**
     * 是否保存登录
     */
    private Boolean saveLogin;
}
