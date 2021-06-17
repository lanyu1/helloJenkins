package com.lanyu.jenkins.hellojenkins.module.base.service.impl;

import com.lanyu.jenkins.hellojenkins.module.base.dao.RoleDepartmentDao;
import com.lanyu.jenkins.hellojenkins.module.base.entity.RoleDepartment;
import com.lanyu.jenkins.hellojenkins.module.base.service.RoleDepartmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * 角色部门service实现类
 * @author lanyu
 * @date 2021年06月17日 10:07
 */
@Slf4j
@Service
@Transactional
public class RoleDepartmentServiceImpl implements RoleDepartmentService {

    @Autowired
    private RoleDepartmentDao roleDepartmentDao;

    /**
     * 初始化dao
     * @return
     */
    @Override
    public RoleDepartmentDao getRepository() {
        return roleDepartmentDao;
    }

    /**
     * 通过roleId获取
     * @param roleId
     * @return
     */
    @Override
    public List<RoleDepartment> findByRoleId(String roleId) {
        return roleDepartmentDao.findByRoleId(roleId);
    }

    /**
     * 通过角色id删除
     * @param roleId
     */
    @Override
    public void deleteByRoleId(String roleId) {
        roleDepartmentDao.deleteByRoleId(roleId);
    }

    /**
     * 通过角色id删除
     * @param departmentId
     */
    @Override
    public void deleteByDepartmentId(String departmentId) {
        roleDepartmentDao.deleteByDepartmentId(departmentId);
    }
}
