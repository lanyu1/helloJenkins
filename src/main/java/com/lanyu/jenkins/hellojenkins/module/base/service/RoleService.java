package com.lanyu.jenkins.hellojenkins.module.base.service;

import com.lanyu.jenkins.hellojenkins.base.LanXiBaseService;
import com.lanyu.jenkins.hellojenkins.module.base.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 角色service接口
 * @author lanyu
 * @date 2021年06月16日 16:10
 */
public interface RoleService extends LanXiBaseService<Role,String> {

    /**
     * 获取默认角色
     * @param defaultRole
     * @return
     */
    List<Role> findByDefaultRole(Boolean defaultRole);

    /**
     * 分页获取
     * @param key
     * @param pageable
     * @return
     */
    Page<Role> findByCondition(String key, Pageable pageable);
}
