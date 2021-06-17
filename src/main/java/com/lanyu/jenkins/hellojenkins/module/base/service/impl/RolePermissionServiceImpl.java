package com.lanyu.jenkins.hellojenkins.module.base.service.impl;

import com.lanyu.jenkins.hellojenkins.base.LanXiBaseDao;
import com.lanyu.jenkins.hellojenkins.module.base.dao.RolePermissionDao;
import com.lanyu.jenkins.hellojenkins.module.base.entity.RolePermission;
import com.lanyu.jenkins.hellojenkins.module.base.service.RolePermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * 角色权限service实现类
 * @author lanyu
 * @date 2021年06月17日 10:01
 */
@Slf4j
@Service
@Transactional
public class RolePermissionServiceImpl implements RolePermissionService {

    @Autowired
    private RolePermissionDao rolePermissionDao;

    /**
     * 初始化dao
     * @return
     */
    @Override
    public RolePermissionDao getRepository() {
        return rolePermissionDao;
    }

    /**
     * 通过permissionId获取
     * @param permissionId
     * @return
     */
    @Override
    public List<RolePermission> findByPermissionId(String permissionId) {
        return rolePermissionDao.findByPermissionId(permissionId);
    }

    /**
     * 通过roleId获取
     * @param roleId
     * @return
     */
    @Override
    public List<RolePermission> findByRoleId(String roleId) {
        return rolePermissionDao.findByRoleId(roleId);
    }

    /**
     * 通过roleId删除
     * @param roleId
     */
    @Override
    public void deleteByRoleId(String roleId) {
        rolePermissionDao.deleteByRoleId(roleId);
    }
}
