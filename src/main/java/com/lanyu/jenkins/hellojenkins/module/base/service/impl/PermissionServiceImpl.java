package com.lanyu.jenkins.hellojenkins.module.base.service.impl;

import com.lanyu.jenkins.hellojenkins.base.LanXiBaseDao;
import com.lanyu.jenkins.hellojenkins.module.base.dao.PermissionDao;
import com.lanyu.jenkins.hellojenkins.module.base.entity.Permission;
import com.lanyu.jenkins.hellojenkins.module.base.service.PermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * 权限菜单service接口实现类
 * @author lanyu
 * @date 2021年06月16日 16:24
 */
@Slf4j
@Service
@Transactional
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionDao permissionDao;

    /**
     * 初始化dao
     * @return
     */
    @Override
    public PermissionDao getRepository() {
        return permissionDao;
    }

    /**
     * 通过parentId查找
     * @param parentId
     * @return
     */
    @Override
    public List<Permission> findByParentIdOrderBySortOrder(String parentId) {
        return permissionDao.findByParentIdOrderBySortOrder(parentId);
    }

    /**
     * 通过类型和状态获取
     * @param type
     * @param status
     * @return
     */
    @Override
    public List<Permission> findByTypeAndStatusOrderBySortOrder(Integer type, Integer status) {
        return permissionDao.findByTypeAndStatusOrderBySortOrder(type,status);
    }

    /**
     * 通过名称获取
     * @param title
     * @return
     */
    @Override
    public List<Permission> findByTitle(String title) {
        return permissionDao.findByTitle(title);
    }

    /**
     * 模糊查询通过名称
     * @param title
     * @return
     */
    @Override
    public List<Permission> findByTitleLikeOrderBySortOrder(String title) {
        return permissionDao.findByTitleLikeOrderBySortOrder(title);
    }
}
