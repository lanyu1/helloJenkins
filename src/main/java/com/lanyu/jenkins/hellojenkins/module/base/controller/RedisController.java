package com.lanyu.jenkins.hellojenkins.module.base.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.lanyu.jenkins.hellojenkins.common.redis.RedisTemplateHelper;
import com.lanyu.jenkins.hellojenkins.common.utils.PageUtil;
import com.lanyu.jenkins.hellojenkins.common.utils.ResultUtil;
import com.lanyu.jenkins.hellojenkins.common.vo.PageVo;
import com.lanyu.jenkins.hellojenkins.common.vo.RedisInfo;
import com.lanyu.jenkins.hellojenkins.common.vo.RedisVo;
import com.lanyu.jenkins.hellojenkins.common.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Redis缓存管理接口
 * @author lanyu
 * @date 2021年08月11日 10:57
 */
@Slf4j
@RestController
@Api(tags = "Redis缓存管理接口")
@RequestMapping("/lanxi/redis")
@Transactional
public class RedisController {

    /**
     * 最大获取10万个键值
     */
    private static final int maxSize = 100000;

    @Autowired
    private RedisTemplateHelper redisTemplate;

    /**
     * 分页获取全部
     * @param key
     * @param pageVo
     * @return
     */
    @RequestMapping(value = "/getAllByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取全部")
    public Result<Page<RedisVo>> getAllByPage(@RequestParam(required = false) String key,
                                              PageVo pageVo) {
        List<RedisVo> list = new ArrayList<>();
        if (StrUtil.isNotBlank(key)) {
            key = "*" + key + "*";
        } else {
            key = "*";
        }
        Set<String> keys = redisTemplate.scan(key);
        int size = keys.size();
        // 限制10万个
        if (size > maxSize) {
            size = maxSize;
        }
        int i = 0;
        for (String s : keys) {
            if (i > size) {
                break;
            }
            RedisVo redisVo = new RedisVo(s, "", null);
            list.add(redisVo);
            i++;
        }
        Page<RedisVo> page = new PageImpl<RedisVo>(PageUtil.listToPage(pageVo, list), PageUtil.initPage(pageVo), size);
        page.getContent().forEach(e -> {
            String value = "";
            try {
                value = redisTemplate.get(e.getKey());
                if (value.length() > 150) {
                    value = value.substring(0, 150) + "...";
                }
            } catch (Exception exception) {
                value = "非字符格式数据";
            }
            e.setValue(value);
            e.setExpireTime(redisTemplate.getExpire(e.getKey(), TimeUnit.SECONDS));
        });
        return new ResultUtil<Page<RedisVo>>().setData(page);
    }

    /**
     * 通过key获取
     * @param key
     * @return
     */
    @RequestMapping(value = "/getByKey/{key}", method = RequestMethod.GET)
    @ApiOperation(value = "通过key获取")
    public Result<Object> getByKey(@PathVariable String key) {

        Map<String, Object> map = new HashMap<>();
        String value = redisTemplate.get(key);
        Long expireTime = redisTemplate.getExpire(key, TimeUnit.SECONDS);
        map.put("value", value);
        map.put("expireTime", expireTime);
        return ResultUtil.data(map);
    }

    /**
     * 添加或编辑
     * @param key
     * @param value
     * @param expireTime
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ApiOperation(value = "添加或编辑")
    public Result<Object> save(@RequestParam String key,
                               @RequestParam String value,
                               @RequestParam Long expireTime) {

        if (expireTime < 0) {
            redisTemplate.set(key, value);
        } else if (expireTime > 0) {
            redisTemplate.set(key, value, expireTime, TimeUnit.SECONDS);
        }
        return ResultUtil.success("保存成功");
    }

    /**
     * 批量删除
     * @param keys
     * @return
     */
    @RequestMapping(value = "/delByKeys", method = RequestMethod.POST)
    @ApiOperation(value = "批量删除")
    public Result<Object> delByKeys(@RequestParam String[] keys) {
        for (String key : keys) {
            redisTemplate.delete(key);
        }
        return ResultUtil.success("删除成功");
    }

    /**
     * 全部删除
     * @return
     */
    @RequestMapping(value = "/delAll", method = RequestMethod.POST)
    @ApiOperation(value = "全部删除")
    public Result<Object> delAll() {
        redisTemplate.deleteByPattern("*");
        return ResultUtil.success("删除成功");
    }

    /**
     * 获取实时key大小
     * @return
     */
    @RequestMapping(value = "/getKeySize", method = RequestMethod.GET)
    @ApiOperation(value = "获取实时key大小")
    public Result<Object> getKeySize() {
        Map<String, Object> map = new HashMap<>(16);
        map.put("keySize", redisTemplate.getConnectionFactory().getConnection().dbSize());
        map.put("time", DateUtil.format(new Date(), "HH:mm:ss"));
        return ResultUtil.data(map);
    }

    /**
     * 获取实时内存大小
     * @return
     */
    @RequestMapping(value = "/getMemory", method = RequestMethod.GET)
    @ApiOperation(value = "获取实时内存大小")
    public Result<Object> getMemory() {
        Map<String, Object> map = new HashMap<>(16);
        Properties memory = redisTemplate.getConnectionFactory().getConnection().info("memory");
        map.put("memory", memory.get("used_memory"));
        map.put("time", DateUtil.format(new Date(), "HH:mm:ss"));
        return ResultUtil.data(map);
    }

    /**
     * 获取Redis信息
     * @return
     */
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    @ApiOperation(value = "获取Redis信息")
    public Result<Object> info() {
        List<RedisInfo> infoList = new ArrayList<>();
        Properties properties = redisTemplate.getConnectionFactory().getConnection().info();
        Set<Object> keys = properties.keySet();
        for (Object key : keys) {
            String value = properties.get(key).toString();
            RedisInfo redisInfo = new RedisInfo();
            redisInfo.setKey(key.toString());
            redisInfo.setValue(value);
            infoList.add(redisInfo);
        }
        return ResultUtil.data(infoList);
    }
}
