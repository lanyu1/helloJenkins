package com.lanyu.jenkins.hellojenkins.module.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lanyu.jenkins.hellojenkins.module.base.entity.Permission;

import java.util.List;

/**
 * 权限菜单service接口（mybatis）
 * @author lanyu
 * @date 2021年06月17日 10:47
 */
public interface IPermissionService extends IService<Permission> {

    /**
     * 通过用户id获取
     * @param userId
     * @return
     */
    List<Permission> findByUserId(String userId);
}
