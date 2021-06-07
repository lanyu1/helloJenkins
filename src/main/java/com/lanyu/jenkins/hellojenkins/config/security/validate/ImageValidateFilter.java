package com.lanyu.jenkins.hellojenkins.config.security.validate;

import cn.hutool.core.util.StrUtil;
import com.lanyu.jenkins.hellojenkins.common.redis.RedisTemplateHelper;
import com.lanyu.jenkins.hellojenkins.common.utils.ResponseUtil;
import com.lanyu.jenkins.hellojenkins.config.properties.CaptchaProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 图形验证码过滤器
 * @author lanyu
 * @date 2021年05月27日 13:38
 */
@Slf4j
@Configuration
public class ImageValidateFilter extends OncePerRequestFilter {

    @Autowired
    private CaptchaProperties captchaProperties;

    @Autowired
    private RedisTemplateHelper redisTemplate;

    @Autowired
    private PathMatcher pathMatcher;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        //判断url是否需要验证
        Boolean flag = false;
        String requestUrl = httpServletRequest.getRequestURI();
        for(String url : captchaProperties.getImage()){
            if (pathMatcher.match(url,requestUrl)){
                flag = true;
                break;
            }
        }
        if (flag){
            String captchaId = httpServletRequest.getParameter("captchaId");
            String code = httpServletRequest.getParameter("code");
            //判断图形验证码参数是否为空
            if (StrUtil.isBlank(captchaId) ||StrUtil.isBlank(code)){
                ResponseUtil.out(httpServletResponse,ResponseUtil.resultMap(false,500,"请传入图形验证码所需参数captchaId或code"));
                return;
            }
            String redisCode = redisTemplate.get(captchaId);
            //判断验证码是否过期
            if (StrUtil.isBlank(redisCode)){
                ResponseUtil.out(httpServletResponse,ResponseUtil.resultMap(false,500,"验证码已过期，请重新获取"));
                return;
            }
            //判断验证码是否正确
            if (!redisCode.toLowerCase().equals(code.toLowerCase())){
                log.info("验证码错误：code:" + code + "，redisCode:" + redisCode);
                ResponseUtil.out(httpServletResponse, ResponseUtil.resultMap(false, 500, "图形验证码输入错误"));
                return;
            }
            //已经验证清除key
            redisTemplate.delete(captchaId);
            //验证成功 放行
            filterChain.doFilter(httpServletRequest,httpServletResponse);
            return;
        }
        //无需验证 直接放行
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }
}
