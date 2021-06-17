package com.lanyu.jenkins.hellojenkins.module.base.service;

import com.lanyu.jenkins.hellojenkins.base.LanXiBaseService;
import com.lanyu.jenkins.hellojenkins.module.base.entity.RoleDepartment;

import java.util.List;

/**
 * 角色部门service接口
 * @author lanyu
 * @date 2021年06月17日 10:05
 */
public interface RoleDepartmentService extends LanXiBaseService<RoleDepartment,String> {

    /**
     * 通过roleId获取
     * @param roleId
     * @return
     */
    List<RoleDepartment> findByRoleId(String roleId);

    /**
     * 通过角色id删除
     * @param roleId
     */
    void deleteByRoleId(String roleId);

    /**
     * 通过角色id删除
     * @param departmentId
     */
    void deleteByDepartmentId(String departmentId);
}
