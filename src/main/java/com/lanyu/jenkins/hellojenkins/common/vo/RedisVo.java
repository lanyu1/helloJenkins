package com.lanyu.jenkins.hellojenkins.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * redisVO
 * @author lanyu
 * @date 2021年08月11日 10:58
 */
@Data
@AllArgsConstructor
public class RedisVo {
    private String key;

    private String value;

    private Long expireTime;
}
