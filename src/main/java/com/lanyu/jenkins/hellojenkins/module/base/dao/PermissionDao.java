package com.lanyu.jenkins.hellojenkins.module.base.dao;

import com.lanyu.jenkins.hellojenkins.base.LanXiBaseDao;
import com.lanyu.jenkins.hellojenkins.module.base.entity.Permission;

import java.util.List;

/**
 * 权限数据处理层
 * @author lanyu
 * @date 2021年06月15日 14:02
 */
public interface PermissionDao extends LanXiBaseDao<Permission,String> {

    /**
     * 通过parentId查找
     * @param parentId
     * @return
     */
    List<Permission> findByParentIdOrderBySortOrder(String parentId);

    /**
     * 通过类型和状态获取
     * @param type
     * @param status
     * @return
     */
    List<Permission> findByTypeAndStatusOrderBySortOrder(Integer type, Integer status);

    /**
     * 通过名称获取
     * @param title
     * @return
     */
    List<Permission> findByTitle(String title);

    /**
     * 模糊查询通过名称
     * @param title
     * @return
     */
    List<Permission> findByTitleLikeOrderBySortOrder(String title);
}
