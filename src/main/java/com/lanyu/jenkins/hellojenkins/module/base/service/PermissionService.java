package com.lanyu.jenkins.hellojenkins.module.base.service;

import com.lanyu.jenkins.hellojenkins.base.LanXiBaseService;
import com.lanyu.jenkins.hellojenkins.module.base.entity.Permission;

import java.util.List;

/**
 * 权限菜单service接口
 *
 * @author lanyu
 * @date 2021年06月16日 16:22
 */
public interface PermissionService extends LanXiBaseService<Permission, String> {

    /**
     * 通过parentId查找
     *
     * @param parentId
     * @return
     */
    List<Permission> findByParentIdOrderBySortOrder(String parentId);

    /**
     * 通过类型和状态获取
     *
     * @param type
     * @param status
     * @return
     */
    List<Permission> findByTypeAndStatusOrderBySortOrder(Integer type, Integer status);

    /**
     * 通过名称获取
     *
     * @param title
     * @return
     */
    List<Permission> findByTitle(String title);

    /**
     * 模糊搜索
     *
     * @param title
     * @return
     */
    List<Permission> findByTitleLikeOrderBySortOrder(String title);
}
