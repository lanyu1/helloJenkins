package com.lanyu.jenkins.hellojenkins.common.constant;

/**
 * 公共常量类
 * @author lanyu
 * @date 2021年06月08日 9:18
 */
public interface CommonConstant {
    /**
     * 正常状态
     */
    Integer STATUS_NORMAL = 0;

    /**
     * 用户默认头像
     */
    String USER_DEFAULT_AVATAR = "https://ooo.0o0.ooo/2019/04/28/5cc5a71a6e3b6.png";

    /**
     * 用户正常状态
     */
    Integer USER_STATUS_NORMAL = 0;

    /**
     * 用户禁用状态
     */
    Integer USER_STATUS_LOCK = -1;

    /**
     * 普通用户
     */
    Integer USER_TYPE_NORMAL = 0;

    /**
     * 全部数据权限
     */
    Integer DATA_TYPE_ALL = 0;


    /**
     * 部门负责人类型 主负责人
     */
    Integer HEADER_TYPE_MAIN = 0;

    /**
     * 部门负责人类型 副负责人
     */
    Integer HEADER_TYPE_VICE = 1;
}
