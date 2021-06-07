package com.lanyu.jenkins.hellojenkins.config.security.jwt;

import cn.hutool.core.util.StrUtil;
import com.lanyu.jenkins.hellojenkins.common.exception.LoginFailLimitException;
import com.lanyu.jenkins.hellojenkins.common.redis.RedisTemplateHelper;
import com.lanyu.jenkins.hellojenkins.common.utils.ResponseUtil;
import com.lanyu.jenkins.hellojenkins.config.properties.LanXiTokenProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * 认证失败处理handler
 *
 * @author lanyu
 * @date 2021年05月27日 13:37
 */
@Slf4j
@Component
public class AuthenticationFailHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private LanXiTokenProperties tokenProperties;

    @Autowired
    private RedisTemplateHelper redisTemplate;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) {
        //用户名找不到  或者 坏的凭证
        if (e instanceof UsernameNotFoundException || e instanceof BadCredentialsException) {
            String userName = request.getParameter("username");
            recordLoginTimes(userName);
            String key = "loginTimeLimit:" + userName;
            String value = redisTemplate.get(key);
            if (StrUtil.isBlank(value)) {
                value = "0";
            }
            //获取已登录错误次数
            //已失败次数
            int loginFailTime = Integer.parseInt(value);
            //还剩次数
            int restLoginTime = tokenProperties.getLoginTimeLimit() - loginFailTime;
            log.info("用户" + userName + "登录失败，还有" + restLoginTime + "次机会");
            if (restLoginTime <= 3 && restLoginTime > 0) {
                ResponseUtil.out(response, ResponseUtil.resultMap(false, 500, "用户名或密码错误，还有" + restLoginTime + "次尝试机会"));
            } else if (restLoginTime <= 0) {
                ResponseUtil.out(response, ResponseUtil.resultMap(false, 500, "登录错误次数超过限制，请" + tokenProperties.getLoginAfterTime() + "分钟后再试"));
            } else {
                ResponseUtil.out(response, ResponseUtil.resultMap(false, 500, "用户名或密码错误"));
            }
        } else if (e instanceof DisabledException) {
            //账户是否被禁用
            ResponseUtil.out(response, ResponseUtil.resultMap(false, 500, "账户被禁用，请联系管理员"));
        } else if (e instanceof LoginFailLimitException) {
            //是否超过登录次数限制
            ResponseUtil.out(response, ResponseUtil.resultMap(false, 500, ((LoginFailLimitException) e).getMsg()));
        } else {
            //其他错误
            ResponseUtil.out(response, ResponseUtil.resultMap(false, 500, "登录失败，其他内部错误"));
        }
    }

    /**
     * 判断用户登录错误次数，并进行记录
     *
     * @param userName
     * @return
     */
    public boolean recordLoginTimes(String userName) {
        String key = "loginTimeLimit:" + userName;
        String flagKey = "loginFailFlag:" + userName;
        String value = redisTemplate.get(key);
        if (StrUtil.isBlank(value)) {
            value = "0";
        }
        //获取已经登录错误次数
        Integer loginFailTime = Integer.parseInt(value) + 1;
        redisTemplate.set(key, loginFailTime.toString(), tokenProperties.getLoginTimeLimit(), TimeUnit.MINUTES);
        if (loginFailTime >= tokenProperties.getLoginTimeLimit()) {
            redisTemplate.set(flagKey, "fail", tokenProperties.getLoginAfterTime(), TimeUnit.MINUTES);
            return false;
        }
        return true;
    }
}
