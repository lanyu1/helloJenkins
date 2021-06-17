package com.lanyu.jenkins.hellojenkins.module.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lanyu.jenkins.hellojenkins.module.base.entity.Role;
import com.lanyu.jenkins.hellojenkins.module.base.entity.UserRole;
import org.apache.ibatis.annotations.Param;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

/**
 * 用户角色service(mybatis)
 * @author lanyu
 * @date 2021年06月17日 10:40
 */
@Cacheable(cacheNames = "userRole")
public interface IUserRoleService extends IService<UserRole> {

    /**
     * 通过用户id获取
     * @param userId
     * @return
     */
    @Cacheable(key = "#userId")
    List<Role> findByUserId(@Param("userId") String userId);

    /**
     * 通过用户id获取用户角色关联的部门数据
     * @param userId
     * @return
     */
    List<String> findDepIdsByUserId(String userId);
}
