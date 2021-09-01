package com.lanyu.jenkins.hellojenkins.common.utils;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lanyu.jenkins.hellojenkins.common.constant.CommonConstant;
import com.lanyu.jenkins.hellojenkins.common.constant.SecurityConstant;
import com.lanyu.jenkins.hellojenkins.common.exception.LanXiException;
import com.lanyu.jenkins.hellojenkins.common.redis.RedisTemplateHelper;
import com.lanyu.jenkins.hellojenkins.common.vo.TokenUser;
import com.lanyu.jenkins.hellojenkins.config.properties.LanXiTokenProperties;
import com.lanyu.jenkins.hellojenkins.module.base.entity.Department;
import com.lanyu.jenkins.hellojenkins.module.base.entity.Role;
import com.lanyu.jenkins.hellojenkins.module.base.entity.User;
import com.lanyu.jenkins.hellojenkins.module.base.service.DepartmentService;
import com.lanyu.jenkins.hellojenkins.module.base.service.IUserRoleService;
import com.lanyu.jenkins.hellojenkins.module.base.service.UserService;
import com.lanyu.jenkins.hellojenkins.module.base.vo.PermissionDTO;
import com.lanyu.jenkins.hellojenkins.module.base.vo.RoleDTO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author lanyu
 * @date 2021年06月07日 18:00
 */
@Slf4j
@Component
public class SecurityUtil {

    @Autowired
    private LanXiTokenProperties lanXiTokenProperties;

    @Autowired
    private RedisTemplateHelper redisTemplate;

    @Autowired
    private IUserRoleService iUserRoleService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private UserService userService;


    /**
     * 获取token
     *
     * @param username
     * @param saveLogin
     * @return
     */
    public String getToken(String username, Boolean saveLogin) {
        if (StrUtil.isBlank(username)) {
            throw new LanXiException("username不能为空");
        }
        Boolean saved = false;
        if (saved == null || saveLogin) {
            saved = true;
            if (!lanXiTokenProperties.getRedis()) {
                lanXiTokenProperties.setTokenExpireTime(lanXiTokenProperties.getSaveLoginTime() * 60 * 24);
            }
        }
        //生成token
        User u = userService.findByUsername(username);
        List<String> list = new ArrayList<>();
        //缓存权限
        if (lanXiTokenProperties.getStorePerms()) {
            for (PermissionDTO p : u.getPermissions()) {
                if (StrUtil.isNotBlank(p.getTitle()) && StrUtil.isNotBlank(p.getPath())) {
                    list.add(p.getTitle());
                }
            }
            for (RoleDTO r : u.getRoles()) {
                list.add(r.getName());
            }
        }
        //登录成功生成token
        String token;
        if (lanXiTokenProperties.getRedis()) {
            //redis
            token = IdUtil.simpleUUID();
            TokenUser tokenUser = new TokenUser(u.getUsername(), list, saved);
            //单设备登录 之前的token失效
            if (lanXiTokenProperties.getSdl()) {
                String oldToken = redisTemplate.get(SecurityConstant.USER_TOKEN + u.getUsername());
                if (StrUtil.isNotBlank(oldToken)) {
                    redisTemplate.delete(SecurityConstant.TOKEN_PRE + oldToken);
                }
            }
            if (saved) {
                redisTemplate.set(SecurityConstant.USER_TOKEN + u.getUsername(), token, lanXiTokenProperties.getSaveLoginTime(), TimeUnit.DAYS);
                redisTemplate.set(SecurityConstant.TOKEN_PRE + token, new Gson().toJson(tokenUser), lanXiTokenProperties.getSaveLoginTime(), TimeUnit.DAYS);
            } else {
                redisTemplate.set(SecurityConstant.USER_TOKEN + u.getUsername(), token, lanXiTokenProperties.getSaveLoginTime(), TimeUnit.MINUTES);
                redisTemplate.set(SecurityConstant.TOKEN_PRE + token, new Gson().toJson(tokenUser), lanXiTokenProperties.getSaveLoginTime(), TimeUnit.MINUTES);
            }
        } else {
            //JWT不缓存权限 避免JWT长度过长
            list = null;
            // JWT
            token = SecurityConstant.TOKEN_SPLIT + Jwts.builder()
                    //主题 放入用户名
                    .setSubject(u.getUsername())
                    //自定义属性 放入用户拥有请求权限
                    .claim(SecurityConstant.AUTHORITIES, new Gson().toJson(list))
                    //失效时间
                    .setExpiration(new Date(System.currentTimeMillis() + lanXiTokenProperties.getTokenExpireTime() * 60 * 1000))
                    //签名算法和密钥
                    .signWith(SignatureAlgorithm.HS512, SecurityConstant.JWT_SIGN_KEY)
                    .compact();
        }
        return token;
    }

    /**
     * 获取当前登录用户信息
     *
     * @return
     */
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getName() == null
                || authentication instanceof AnonymousAuthenticationToken) {
            throw new LanXiException("未检测到登录用户");
        }
        return userService.findByUsername(authentication.getName());
    }

    public List<String> getDepartmentIds() {
        List<String> departmentIds = new ArrayList<>();
        User u = getCurrentUser();
        //读取缓存数据
        String key = "userRole::depIds" + u.getId();
        String v = redisTemplate.get(key);
        if (StrUtil.isNotBlank(v)) {
            departmentIds = new Gson().fromJson(v, new TypeToken<List<String>>() {
            }.getType());
            return departmentIds;
        }
        //当前用户拥有的角色
        List<Role> roles = iUserRoleService.findByUserId(u.getId());
        //判断有无全部数据的角色
        Boolean flagAll = false;
        for (Role r : roles) {
            if (r.getDataType() == null || r.getDataType().equals(CommonConstant.DATA_TYPE_ALL)) {
                flagAll = true;
                break;
            }
        }
        //包含全部权限返回null
        if (flagAll) {
            return null;
        }
        //每个角色判断求并集
        for (Role r : roles) {
            if (r.getDataType().equals(CommonConstant.DATA_TYPE_UNDER)) {
                //本部门及一下
                if (StrUtil.isBlank(u.getDepartmentId())) {
                    //用户无部门
                    departmentIds.add("-1");
                } else {
                    //递归获取自己与子集
                    List<String> ids = new ArrayList<>();
                    getRecursion(u.getDepartmentId(), ids);
                    departmentIds.addAll(ids);
                }
            } else if (r.getDataType().equals(CommonConstant.DATA_TYPE_SAME)) {
                //本部门
                if (StrUtil.isBlank(u.getDepartmentId())) {
                    //用户无部门
                    departmentIds.add("-1");
                } else {
                    departmentIds.add(u.getDepartmentId());
                }
            } else if (r.getDataType().equals(CommonConstant.DATA_TYPE_CUSTOM)) {
                //自定义
                List<String> deptIds = iUserRoleService.findDepIdsByUserId(u.getId());
                if (deptIds == null || deptIds.size() == 0) {
                    departmentIds.add("-1");
                } else {
                    departmentIds.addAll(deptIds);
                }
            }
        }
        //去重
        LinkedHashSet<String> set = new LinkedHashSet<>(departmentIds.size());
        set.addAll(departmentIds);
        departmentIds.clear();
        departmentIds.addAll(set);
        //缓存
        redisTemplate.set(key, new Gson().toJson(departmentIds), 15L, TimeUnit.DAYS);
        return departmentIds;
    }

    /**
     * 通过用户名获取用户拥有权限
     * @param username
     * @return
     */
    public List<GrantedAuthority> getCurrentUserPermissions(String username){
        List<GrantedAuthority> authorities = new ArrayList<>();
        User user = userService.findByUsername(username);
        if (user == null || user.getPermissions() == null || user.getPermissions().isEmpty()){
            return authorities;
        }
        for (PermissionDTO p : user.getPermissions()){
            authorities.add(new SimpleGrantedAuthority(p.getTitle()));
        }
        return authorities;
    }

    /**
     * 递归查询部门信息
     *
     * @param departmentId
     * @param ids
     */
    private void getRecursion(String departmentId, List<String> ids) {
        Department department = departmentService.get(departmentId);
        ids.add(department.getId());
        if (department.getIsParent() != null && department.getIsParent()) {
            //获取其下级
            List<Department> departments = departmentService.findByParentIdAndStatusOrderBySortOrder(departmentId, CommonConstant.STATUS_NORMAL);
            departments.forEach(d -> {
                getRecursion(d.getId(), ids);
            });
        }
    }
}

