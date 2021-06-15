package com.lanyu.jenkins.hellojenkins.module.base.dao;

import com.lanyu.jenkins.hellojenkins.base.LanXiBaseDao;
import com.lanyu.jenkins.hellojenkins.module.base.entity.Role;

import java.util.List;

/**
 * 角色数据处理层
 * @author lanyu
 * @date 2021年06月15日 13:50
 */
public interface RoleDao extends LanXiBaseDao<Role,String> {

    /**
     * 获取默认角色
     * @param defaultRole
     * @return
     */
    List<Role> findByDefaultRole(Boolean defaultRole);
}
