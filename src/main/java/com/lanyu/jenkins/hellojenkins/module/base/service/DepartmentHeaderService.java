package com.lanyu.jenkins.hellojenkins.module.base.service;

import com.lanyu.jenkins.hellojenkins.base.LanXiBaseService;
import com.lanyu.jenkins.hellojenkins.common.vo.UserVo;
import com.lanyu.jenkins.hellojenkins.module.base.entity.DepartmentHeader;

import java.util.List;

/**
 * 部门负责人service接口
 * @author lanyu
 * @date 2021年06月17日 9:44
 */
public interface DepartmentHeaderService extends LanXiBaseService<DepartmentHeader,String> {

    /**
     * 通过部门和负责人类型获取
     * @param departmentId
     * @param type
     * @return
     */
    List<UserVo> findHeaderByDepartmentId(String departmentId, Integer type);

    /**
     * 通过部门获取
     * @param departmentIds
     * @return
     */
    List<DepartmentHeader> findByDepartmentIdIn(List<String> departmentIds);

    /**
     * 通过部门id删除
     * @param departmentId
     */
    void deleteByDepartmentId(String departmentId);

    /**
     * 通过userId删除
     * @param userId
     */
    void deleteByUserId(String userId);

    /**
     * 是否为部门负责人
     * @param userId
     * @param departmentId
     * @return
     */
    Boolean isDepartmentHeader(String userId, String departmentId);
}
