package com.lanyu.jenkins.hellojenkins.module.base.service;

import com.lanyu.jenkins.hellojenkins.base.LanXiBaseService;
import com.lanyu.jenkins.hellojenkins.module.base.entity.UserRole;

import java.util.List;

/**
 * 用户角色接口service
 * @author lanyu
 * @date 2021年06月16日 16:19
 */
public interface UserRoleService extends LanXiBaseService<UserRole,String> {

    /**
     * 通过roleId查找
     * @param roleId
     * @return
     */
    List<UserRole> findByRoleId(String roleId);

    /**
     * 删除用户角色
     * @param userId
     */
    void deleteByUserId(String userId);
}
