package com.lanyu.jenkins.hellojenkins.module.base.service.impl.mybatis;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lanyu.jenkins.hellojenkins.module.base.dao.mapper.PermissionMapper;
import com.lanyu.jenkins.hellojenkins.module.base.entity.Permission;
import com.lanyu.jenkins.hellojenkins.module.base.service.IPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 权限菜单service实现类（mybatis）
 * @author lanyu
 * @date 2021年06月17日 10:48
 */
@Service
public class IPermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements IPermissionService {

    @Autowired
    private PermissionMapper permissionMapper;

    /**
     * 通过用户id获取
     * @param userId
     * @return
     */
    @Override
    public List<Permission> findByUserId(String userId) {
        return permissionMapper.findByUserId(userId);
    }
}
