package com.lanyu.jenkins.hellojenkins.common.constant;

import cn.hutool.core.util.IdUtil;

/**
 * security常量类
 * @author lanyu
 * @date 2021年06月07日 17:05
 */
public interface SecurityConstant {

    /**
     * token参数头
     */
    String HEADER = "accessToken";
    /**
     * 用户选择JWT保存时间参数头
     */
    String SAVE_LOGIN = "saveLogin";

    /**
     * 用户token前缀key 单点登录使用
     */
    String USER_TOKEN = "LANXI_USER_TOKEN:";

    /**
     * 交互token前缀key
     */
    String TOKEN_PRE = "LANXI_TOKEN_PRE:";

    /**
     * token分割
     */
    String TOKEN_SPLIT = "Bearer ";

    /**
     * 权限参数头
     */
    String AUTUORITIES = "authorities";

    /**
     * JWT签名加密key
     */
    String JWT_SIGN_KEY = IdUtil.simpleUUID();

    /**
     * 权限参数头
     */
    String AUTHORITIES = "authorities";
}
