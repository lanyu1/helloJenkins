package com.lanyu.jenkins.hellojenkins.config.security.jwt;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.google.gson.Gson;
import com.lanyu.jenkins.hellojenkins.common.annotation.SystemLog;
import com.lanyu.jenkins.hellojenkins.common.constant.SecurityConstant;
import com.lanyu.jenkins.hellojenkins.common.enums.LogType;
import com.lanyu.jenkins.hellojenkins.common.redis.RedisTemplateHelper;
import com.lanyu.jenkins.hellojenkins.common.utils.IpInfoUtil;
import com.lanyu.jenkins.hellojenkins.common.utils.ResponseUtil;
import com.lanyu.jenkins.hellojenkins.common.vo.TokenUser;
import com.lanyu.jenkins.hellojenkins.config.properties.LanXiTokenProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 登录成功处理类
 * @author lanyu
 * @date 2021年05月27日 13:37
 */
@Slf4j
@Component
public class AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    private LanXiTokenProperties lanXiTokenProperties;

    @Autowired
    private IpInfoUtil ipInfoUtil;

    private RedisTemplateHelper redisTemplate;

    /**
     * 登录成功处理方法
     * @param request
     * @param response
     * @param authentication
     * @throws IOException
     */
    @Override
    @SystemLog(description = "登录系统",type = LogType.LOGIN)
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        //用户选择保存登录状态几天（记住我）
        String saveLogin = request.getParameter(SecurityConstant.SAVE_LOGIN);
        Boolean saved = false;
        //判断是否保存登录状态
        if (StrUtil.isNotBlank(saveLogin) && Boolean.valueOf(saveLogin)){
            saved = true;
            if (lanXiTokenProperties.getRedis()){
                lanXiTokenProperties.setTokenExpireTime(lanXiTokenProperties.getSaveLoginTime() * 60 * 24);
            }
        }
        //获取登录用户名
        String userName = ((UserDetails)authentication.getPrincipal()).getUsername();
        //获取用户权限列表
        List<GrantedAuthority> authorities = (List<GrantedAuthority>)((UserDetails)authentication.getPrincipal()).getAuthorities();
        List<String> list = new ArrayList<>();
        for (GrantedAuthority g : authorities){
            list.add(g.getAuthority());
        }
        //获取url信息
        ipInfoUtil.getUrl(request);
        //登录成功生成token
        String token;
        if (lanXiTokenProperties.getRedis()){
            //redis
            //获取token信息
            token = IdUtil.simpleUUID();
            TokenUser user = new TokenUser(userName,list,saved);
            //不缓存权限
            if (!lanXiTokenProperties.getStorePerms()){
                user.setPermissions(null);
            }
            //单设备登录，之前的token失效
            if (lanXiTokenProperties.getSdl()){
                String oldToken = redisTemplate.get(SecurityConstant.USER_TOKEN +userName);
                if (StrUtil.isNotBlank(oldToken)){
                    redisTemplate.delete(SecurityConstant.TOKEN_PRE + oldToken);
                }
            }
            //保存登录用户token信息（记住我）
            if (saved){
                redisTemplate.set(SecurityConstant.USER_TOKEN + userName, token, lanXiTokenProperties.getSaveLoginTime(), TimeUnit.DAYS);
                redisTemplate.set(SecurityConstant.TOKEN_PRE + token, new Gson().toJson(user), lanXiTokenProperties.getSaveLoginTime(), TimeUnit.DAYS);
            }else{
                redisTemplate.set(SecurityConstant.USER_TOKEN + userName, token, lanXiTokenProperties.getTokenExpireTime(), TimeUnit.MINUTES);
                redisTemplate.set(SecurityConstant.TOKEN_PRE + token, new Gson().toJson(user), lanXiTokenProperties.getTokenExpireTime(), TimeUnit.MINUTES);
            }
        }else{
            //JWT不缓存权限 避免JWT长度过长
            list = null;
            //JWT
            token = SecurityConstant.TOKEN_SPLIT + Jwts.builder()
                    //主题 放入用户名
                    .setSubject(userName)
                    //自定义属性 放入用户拥有请求权限
                    .claim(SecurityConstant.AUTUORITIES,new Gson().toJson(list))
                    //失效时间
                    .setExpiration(new Date(System.currentTimeMillis() + lanXiTokenProperties.getTokenExpireTime() *60 * 1000))
                    //签名算法和秘钥
                    .signWith(SignatureAlgorithm.HS512,SecurityConstant.JWT_SIGN_KEY)
                    .compact();
        }
        ResponseUtil.out(response,ResponseUtil.resultMap(true,200,"登录成功",token));
    }


}
