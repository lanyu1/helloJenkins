package com.lanyu.jenkins.hellojenkins.config.security.permission;

import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.util.PathMatcher;

import java.util.*;

/**
 * 权限资源管理器
 * 为权限决断器提供支持
 * @author lanyu
 * @date 2021年05月27日 13:38
 */
public class MySecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    @Autowired
    private PathMatcher pathMatcher;

    /**
     * 加载权限表中所有操作请求权限
     */
    public void loadResourceDefine() {

//        map = new HashMap<>(16);
//        Collection<ConfigAttribute> configAttributes;
//        ConfigAttribute cfg;
//        // 获取启用的权限操作请求
//        List<Permission> permissions = permissionService.findByTypeAndStatusOrderBySortOrder(CommonConstant.PERMISSION_OPERATION, CommonConstant.STATUS_NORMAL);
//        for (Permission permission : permissions) {
//            if (StrUtil.isNotBlank(permission.getTitle()) && StrUtil.isNotBlank(permission.getPath())) {
//                configAttributes = new ArrayList<>();
//                cfg = new SecurityConfig(permission.getTitle());
//                // 作为MyAccessDecisionManager类的decide的第三个参数
//                configAttributes.add(cfg);
//                // 用权限的path作为map的key，用ConfigAttribute的集合作为value
//                map.put(permission.getPath(), configAttributes);
//            }
//        }
    }

    private Map<String, Collection<ConfigAttribute>> map = null;

    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return false;
    }
}
