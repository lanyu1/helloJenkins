package com.lanyu.jenkins.hellojenkins.base;

import com.lanyu.jenkins.hellojenkins.common.utils.PageUtil;
import com.lanyu.jenkins.hellojenkins.common.utils.ResultUtil;
import com.lanyu.jenkins.hellojenkins.common.vo.PageVo;
import com.lanyu.jenkins.hellojenkins.common.vo.Result;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.Serializable;
import java.util.List;

/**
 * 基础控制器controller类
 * @author lanyu
 * @date 2021年06月08日 11:32
 */
public abstract class LanXiBaseController<E,ID extends Serializable> {

    /**
     * 获取service
     * @return
     */
    @Autowired
    public abstract LanXiBaseService<E,ID> getBaseService();

    /**
     * 通过id获取信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/get/{id}",method = RequestMethod.GET)
    @ResponseBody
    @ApiModelProperty(value = "通过id获取")
    public Result<E> get(@PathVariable ID id){
        E entity = getBaseService().get(id);
        return new ResultUtil<E>().setData(entity);
    }

    /**
     * 获取全部数据
     * @return
     */
    @RequestMapping(value = "/getAll",method = RequestMethod.GET)
    @ResponseBody
    @ApiModelProperty(value = "获取全部数据")
    public Result<List<E>> getAll(){
        List<E> list = getBaseService().getAll();
        return new ResultUtil<List<E>>().setData(list);
    }

    /**
     * 分页获取数据
     * @param page
     * @return
     */
    @RequestMapping(value = "/getByPage",method = RequestMethod.GET)
    @ResponseBody
    @ApiModelProperty(value = "分页获取数据")
    public Result<Page<E>> getByPage(PageVo page){
        Page<E> data = getBaseService().findAll(PageUtil.initPage(page));
        return new ResultUtil<Page<E>>().setData(data);
    }

    /**
     * 保存数据
     * @param entity
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "保存数据")
    public Result<E> save(E entity) {
        E e = getBaseService().save(entity);
        return new ResultUtil<E>().setData(e);
    }

    /**
     * 更新数据
     * @param entity
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    @ResponseBody
    @ApiOperation(value = "更新数据")
    public Result<E> update(E entity) {

        E e = getBaseService().update(entity);
        return new ResultUtil<E>().setData(e);
    }

    /**
     * 批量根据id删除
     * @param ids
     * @return
     */
    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delByIds(ID[] ids) {

        for (ID id : ids) {
            getBaseService().delete(id);
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }
}
