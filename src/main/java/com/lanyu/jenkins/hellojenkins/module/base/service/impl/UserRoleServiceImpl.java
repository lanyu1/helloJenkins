package com.lanyu.jenkins.hellojenkins.module.base.service.impl;

import com.lanyu.jenkins.hellojenkins.module.base.dao.UserRoleDao;
import com.lanyu.jenkins.hellojenkins.module.base.entity.UserRole;
import com.lanyu.jenkins.hellojenkins.module.base.service.UserRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * 用户角色接口service实现类
 *
 * @author lanyu
 * @date 2021年06月16日 16:19
 */
@Slf4j
@Service
@Transactional
public class UserRoleServiceImpl implements UserRoleService {

    @Autowired
    private UserRoleDao userRoleDao;

    /**
     * 初始化dao
     *
     * @return
     */
    @Override
    public UserRoleDao getRepository() {
        return userRoleDao;
    }

    /**
     * 通过roleId查找
     * @param roleId
     * @return
     */
    @Override
    public List<UserRole> findByRoleId(String roleId) {
        return userRoleDao.findByRoleId(roleId);
    }

    /**
     * 删除用户角色
     * @param userId
     */
    @Override
    public void deleteByUserId(String userId) {
        userRoleDao.deleteByUserId(userId);
    }
}
