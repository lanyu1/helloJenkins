package com.lanyu.jenkins.hellojenkins.module.base.controller;

import com.lanyu.jenkins.hellojenkins.common.redis.RedisTemplateHelper;
import com.lanyu.jenkins.hellojenkins.common.utils.PageUtil;
import com.lanyu.jenkins.hellojenkins.common.utils.ResultUtil;
import com.lanyu.jenkins.hellojenkins.common.vo.PageVo;
import com.lanyu.jenkins.hellojenkins.common.vo.Result;
import com.lanyu.jenkins.hellojenkins.module.base.entity.Dict;
import com.lanyu.jenkins.hellojenkins.module.base.entity.DictData;
import com.lanyu.jenkins.hellojenkins.module.base.service.DictDataService;
import com.lanyu.jenkins.hellojenkins.module.base.service.DictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 字典数据管理controller
 * @author lanyu
 * @date 2021年08月11日 10:55
 */
@Slf4j
@RestController
@Api(tags = "字典数据管理接口")
@RequestMapping("/lanxi/dictData")
@CacheConfig(cacheNames = "dictData")
@Transactional
public class DictDataController {

    @Autowired
    private DictService dictService;

    @Autowired
    private DictDataService dictDataService;

    @Autowired
    private RedisTemplateHelper redisTemplate;

    /**
     * 多条件分页获取字典数据列表
     * @param dictData
     * @param pageVo
     * @return
     */
    @RequestMapping(value = "/getByCondition", method = RequestMethod.GET)
    @ApiOperation(value = "多条件分页获取字典数据列表")
    public Result<Page<DictData>> getByCondition(DictData dictData,
                                                 PageVo pageVo) {
        Page<DictData> page = dictDataService.findByCondition(dictData, PageUtil.initPage(pageVo));
        return new ResultUtil<Page<DictData>>().setData(page);
    }

    /**
     * 通过类型获取
     * @param type
     * @return
     */
    @RequestMapping(value = "/getByType/{type}", method = RequestMethod.GET)
    @ApiOperation(value = "通过类型获取")
    @Cacheable(key = "#type")
    public Result<Object> getByType(@PathVariable String type) {
        Dict dict = dictService.findByType(type);
        if (dict == null) {
            return ResultUtil.error("字典类型 " + type + " 不存在");
        }
        List<DictData> list = dictDataService.findByDictId(dict.getId());
        return ResultUtil.data(list);
    }

    /**
     * 添加数据字典
     * @param dictData
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation(value = "添加")
    public Result<Object> add(DictData dictData) {
        Dict dict = dictService.get(dictData.getDictId());
        if (dict == null) {
            return ResultUtil.error("字典类型id不存在");
        }
        dictDataService.save(dictData);
        // 删除缓存
        redisTemplate.delete("dictData::" + dict.getType());
        return ResultUtil.success("添加成功");
    }

    /**
     * 编辑数据字典
     * @param dictData
     * @return
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation(value = "编辑")
    public Result<Object> edit(DictData dictData) {
        dictDataService.update(dictData);
        // 删除缓存
        Dict dict = dictService.get(dictData.getDictId());
        redisTemplate.delete("dictData::" + dict.getType());
        return ResultUtil.success("编辑成功");
    }

    /**
     * 批量通过id删除
     * @param ids
     * @return
     */
    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delByIds(@RequestParam String[] ids) {
        for (String id : ids) {
            DictData dictData = dictDataService.get(id);
            if (dictData == null) {
                return ResultUtil.error("数据不存在");
            }
            Dict dict = dictService.get(dictData.getDictId());
            dictDataService.delete(id);
            // 删除缓存
            redisTemplate.delete("dictData::" + dict.getType());
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }
}
