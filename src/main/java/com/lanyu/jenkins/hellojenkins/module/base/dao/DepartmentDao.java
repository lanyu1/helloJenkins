package com.lanyu.jenkins.hellojenkins.module.base.dao;

import com.lanyu.jenkins.hellojenkins.base.LanXiBaseDao;
import com.lanyu.jenkins.hellojenkins.module.base.entity.Department;

import java.util.List;

/**
 * 部门数据处理层
 * @author lanyu
 * @date 2021年06月15日 14:11
 */
public interface DepartmentDao extends LanXiBaseDao<Department,String> {

    /**
     * 通过父id获取  升序
     * @param parentId
     * @return
     */
    List<Department> findByParentIdOrderBySortOrder(String parentId);

    /**
     * 通过父id获取 升序 数据权限
     * @param parentId
     * @param departmentIds
     * @return
     */
    List<Department> findByParentIdAndIdInOrderBySortOrder(String parentId,List<String> departmentIds);

    /**
     * 通过父id和状态获取 升序
     * @param parentId
     * @param status
     * @return
     */
    List<Department> findByParentIdAndStatusOrderBySortOrder(String parentId, Integer status);

    /**
     * 部门名称模糊搜索 升序
     * @param title
     * @return
     */
    List<Department> findByTitleLikeOrderBySortOrder(String title);

    /**
     * 部门名模糊搜索 升序 数据权限
     * @param title
     * @param departmentIds
     * @return
     */
    List<Department> findByTitleLikeAndIdInOrderBySortOrder(String title, List<String> departmentIds);
}
