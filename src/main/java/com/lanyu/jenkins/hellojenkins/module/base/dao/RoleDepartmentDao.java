package com.lanyu.jenkins.hellojenkins.module.base.dao;

import com.lanyu.jenkins.hellojenkins.base.LanXiBaseDao;
import com.lanyu.jenkins.hellojenkins.module.base.entity.RoleDepartment;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 角色部门数据处理层
 * @author lanyu
 * @date 2021年06月15日 13:58
 */
public interface RoleDepartmentDao extends LanXiBaseDao<RoleDepartment,String> {

    /**
     * 根据roleId获取
     * @param roleId
     * @return
     */
    List<RoleDepartment> findByRoleId(String roleId);

    /**
     * 通过角色id删除
     * @param roleId
     */
    @Modifying
    @Query("delete from RoleDepartment r where r.roleId=?1")
    void deleteByRoleId(String roleId);

    /**
     * 根据部门id删除
     * @param departmentId
     */
    @Modifying
    @Query("delete from RoleDepartment r where r.departmentId=?1")
    void deleteByDepartmentId(String departmentId);
}
