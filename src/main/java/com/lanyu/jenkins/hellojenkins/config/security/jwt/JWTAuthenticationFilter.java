package com.lanyu.jenkins.hellojenkins.config.security.jwt;

import cn.hutool.core.util.StrUtil;
import com.google.gson.Gson;
import com.lanyu.jenkins.hellojenkins.common.constant.SecurityConstant;
import com.lanyu.jenkins.hellojenkins.common.redis.RedisTemplateHelper;
import com.lanyu.jenkins.hellojenkins.common.utils.ResponseUtil;
import com.lanyu.jenkins.hellojenkins.common.utils.SecurityUtil;
import com.lanyu.jenkins.hellojenkins.common.vo.TokenUser;
import com.lanyu.jenkins.hellojenkins.config.properties.LanXiTokenProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * jwt认证失败处理类
 * @author lanyu
 * @date 2021年05月27日 13:37
 */
@Slf4j
public class JWTAuthenticationFilter extends BasicAuthenticationFilter {

    @Autowired
    private LanXiTokenProperties lanXiTokenProperties;

    @Autowired
    private RedisTemplateHelper redisTemplate;

    @Autowired
    private SecurityUtil securityUtil;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager,
                                   LanXiTokenProperties lanXiTokenProperties,
                                   RedisTemplateHelper redisTemplate,
                                   SecurityUtil securityUtil) {
        super(authenticationManager);
        this.lanXiTokenProperties = lanXiTokenProperties;
        this.redisTemplate = redisTemplate;
        this.securityUtil = securityUtil;
    }
    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, AuthenticationEntryPoint authenticationEntryPoint){
        super(authenticationManager,authenticationEntryPoint);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader(SecurityConstant.HEADER);
        //获取请求头
        if (StrUtil.isBlank(header)){
            header = request.getParameter(SecurityConstant.HEADER);
        }
        Boolean notValid = StrUtil.isBlank(header) || (!lanXiTokenProperties.getRedis() && !header.startsWith(SecurityConstant.TOKEN_SPLIT));
        if (notValid){
            chain.doFilter(request,response);
            return;
        }
        UsernamePasswordAuthenticationToken authentication = null;
        if (StrUtil.isNotBlank(header)){
            authentication = getAuthentication(header,response);
        }
     }

    /**
     * 获取用户token信息
     * @param header
     * @param response
     * @return
     */
     private UsernamePasswordAuthenticationToken getAuthentication(String header,HttpServletResponse response){
         // 用户名
         String username = null;
         // 权限
         List<GrantedAuthority> authorities = new ArrayList<>();

         if (lanXiTokenProperties.getRedis()) {
             // redis
             String v = redisTemplate.get(SecurityConstant.TOKEN_PRE + header);
             if (StrUtil.isBlank(v)) {
                 ResponseUtil.out(response, ResponseUtil.resultMap(false, 401, "登录已失效，请重新登录"));
                 return null;
             }
             TokenUser user = new Gson().fromJson(v, TokenUser.class);
             username = user.getUserName();
             if (lanXiTokenProperties.getStorePerms()) {
                 // 缓存了权限
                 for (String ga : user.getPermissions()) {
                     authorities.add(new SimpleGrantedAuthority(ga));
                 }
             } else {
                 // 未缓存 读取权限数据
                 authorities = securityUtil.getCurrentUserPermissions(username);
             }
             if (!user.getSaveLogin()) {
                 // 若未保存登录状态重新设置失效时间
                 redisTemplate.set(SecurityConstant.USER_TOKEN + username, header, lanXiTokenProperties.getTokenExpireTime(), TimeUnit.MINUTES);
                 redisTemplate.set(SecurityConstant.TOKEN_PRE + header, v, lanXiTokenProperties.getTokenExpireTime(), TimeUnit.MINUTES);
             }
         } else {
             // JWT
             try {
                 // 解析token
                 Claims claims = Jwts.parser()
                         .setSigningKey(SecurityConstant.JWT_SIGN_KEY)
                         .parseClaimsJws(header.replace(SecurityConstant.TOKEN_SPLIT, ""))
                         .getBody();

                 // 获取用户名
                 username = claims.getSubject();
                 // JWT不缓存权限 读取权限数据 避免JWT长度过长
                 authorities = securityUtil.getCurrentUserPermissions(username);
             } catch (ExpiredJwtException e) {
                 ResponseUtil.out(response, ResponseUtil.resultMap(false, 401, "登录已失效，请重新登录"));
             } catch (Exception e) {
                 log.error(e.toString());
                 ResponseUtil.out(response, ResponseUtil.resultMap(false, 500, "解析token错误"));
             }
         }

         if (StrUtil.isNotBlank(username)) {
             // 踩坑提醒 此处password不能为null
             User principal = new User(username, "", authorities);
             return new UsernamePasswordAuthenticationToken(principal, null, authorities);
         }
         return null;
     }
}
