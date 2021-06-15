package com.lanyu.jenkins.hellojenkins.module.base.dao;

import com.lanyu.jenkins.hellojenkins.base.LanXiBaseDao;
import com.lanyu.jenkins.hellojenkins.module.base.entity.RolePermission;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 角色权限数据处理层
 * @author lanyu
 * @date 2021年06月15日 14:07
 */
public interface RolePermissionDao extends LanXiBaseDao<RolePermission,String> {

    /**
     * 通过permissionId获取
     * @param permissionId
     * @return
     */
    List<RolePermission> findByPermissionId(String permissionId);

    /**
     * 通过roleId获取
     * @param roleId
     * @return
     */
    List<RolePermission> findByRoleId(String roleId);

    /**
     * 通过roleId删除
     * @param roleId
     */
    @Modifying
    @Query("delete from RolePermission r where r.roleId=?1")
    void deleteByRoleId(String roleId);
}
