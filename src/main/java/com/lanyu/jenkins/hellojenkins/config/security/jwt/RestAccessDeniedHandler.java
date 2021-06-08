package com.lanyu.jenkins.hellojenkins.config.security.jwt;

import com.lanyu.jenkins.hellojenkins.common.utils.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 无权限访问处理类
 * @author lanyu
 * @date 2021年05月27日 13:37
 */
@Slf4j
@Component
public class RestAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        ResponseUtil.out(httpServletResponse,ResponseUtil.resultMap(false,403,"抱歉您没有访问权限"));
    }
}
