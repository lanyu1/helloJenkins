package com.lanyu.jenkins.hellojenkins.module.base.dao;

import com.lanyu.jenkins.hellojenkins.base.LanXiBaseDao;
import com.lanyu.jenkins.hellojenkins.module.base.entity.DepartmentHeader;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 部门负责人数据处理层
 * @author lanyu
 * @date 2021年06月15日 14:18
 */
public interface DepartmentHeaderDao extends LanXiBaseDao<DepartmentHeader,String> {

    /**
     * 通过部门id和负责人类型获取
     * @param departmentId
     * @param type
     * @return
     */
    List<DepartmentHeader> findByDepartmentIdAndType(String departmentId, Integer type);

    /**
     * 通过部门获取
     * @param departmentIds
     * @return
     */
    List<DepartmentHeader> findByDepartmentIdIn(List<String> departmentIds);

    /**
     * 通过部门id和userId类型获取
     * @param userId
     * @param departmentId
     * @return
     */
    List<DepartmentHeader> findByUserIdAndDepartmentId(String userId, String departmentId);

    /**
     * 通过部门id删除
     * @param departmentId
     */
    @Modifying
    @Query("delete from DepartmentHeader d where d.departmentId = ?1")
    void deleteByDepartmentId(String departmentId);

    /**
     * 通过userId删除
     * @param userId
     */
    @Modifying
    @Query("delete from DepartmentHeader d where d.userId = ?1")
    void deleteByUserId(String userId);

}
