package com.lanyu.jenkins.hellojenkins.common.utils;

import com.lanyu.jenkins.hellojenkins.common.redis.RedisTemplateHelper;
import com.lanyu.jenkins.hellojenkins.config.properties.LanXiTokenProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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


}
