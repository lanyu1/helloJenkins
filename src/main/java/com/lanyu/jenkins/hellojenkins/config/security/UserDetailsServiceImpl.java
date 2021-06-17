package com.lanyu.jenkins.hellojenkins.config.security;

import cn.hutool.core.util.StrUtil;
import com.lanyu.jenkins.hellojenkins.common.exception.LoginFailLimitException;
import com.lanyu.jenkins.hellojenkins.common.redis.RedisTemplateHelper;
import com.lanyu.jenkins.hellojenkins.common.utils.NameUtil;
import com.lanyu.jenkins.hellojenkins.module.base.entity.User;
import com.lanyu.jenkins.hellojenkins.module.base.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 用户details实现类
 * @author lanyu
 * @date 2021年05月27日 13:36
 */
@Slf4j
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private RedisTemplateHelper redisTemplate;

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String flagKey = "loginFailFlag:" + username;
        String value = redisTemplate.get(flagKey);
        Long timeRest = redisTemplate.getExpire(flagKey, TimeUnit.MINUTES);
        if (StrUtil.isNotBlank(value)) {
            // 超过限制次数
            throw new LoginFailLimitException("登录错误次数超过限制，请" + timeRest + "分钟后再试");
        }
        User user;
        if (NameUtil.mobile(username)) {
            user = userService.findByMobile(username);
        } else if (NameUtil.email(username)) {
            user = userService.findByEmail(username);
        } else {
            user = userService.findByUsername(username);
        }
        return new SecurityUserDetails(user);
    }
}
