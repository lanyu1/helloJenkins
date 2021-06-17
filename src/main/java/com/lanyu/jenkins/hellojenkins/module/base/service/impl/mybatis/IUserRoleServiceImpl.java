package com.lanyu.jenkins.hellojenkins.module.base.service.impl.mybatis;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lanyu.jenkins.hellojenkins.module.base.dao.mapper.UserRoleMapper;
import com.lanyu.jenkins.hellojenkins.module.base.entity.Role;
import com.lanyu.jenkins.hellojenkins.module.base.entity.UserRole;
import com.lanyu.jenkins.hellojenkins.module.base.service.IUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户角色service实现类（mybatis）
 *
 * @author lanyu
 * @date 2021年06月17日 10:44
 */
@Service
public class IUserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {

    @Autowired
    private UserRoleMapper userRoleMapper;

    /**
     * 通过用户id获取
     * @param userId
     * @return
     */
    @Override
    public List<Role> findByUserId(String userId) {
        return userRoleMapper.findByUserId(userId);
    }

    /**
     * 通过用户id获取用户角色关联的部门数据
     * @param userId
     * @return
     */
    @Override
    public List<String> findDepIdsByUserId(String userId) {
        return userRoleMapper.findDepIdsByUserId(userId);
    }
}
