package com.lanyu.jenkins.hellojenkins.module.base.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lanyu.jenkins.hellojenkins.module.base.entity.Permission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 权限mapper数据处理层
 * @author lanyu
 * @date 2021年06月15日 14:52
 */
public interface PermissionMapper extends BaseMapper<Permission> {
    /**
     * 通过用户id获取
     * @param userId
     * @return
     */
    List<Permission> findByUserId(@Param("userId") String userId);
}
