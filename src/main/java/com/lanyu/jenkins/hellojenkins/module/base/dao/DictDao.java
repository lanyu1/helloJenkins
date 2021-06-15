package com.lanyu.jenkins.hellojenkins.module.base.dao;

import com.lanyu.jenkins.hellojenkins.base.LanXiBaseDao;
import com.lanyu.jenkins.hellojenkins.module.base.entity.Dict;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 字典数据处理层
 * @author lanyu
 * @date 2021年06月15日 14:33
 */
public interface DictDao extends LanXiBaseDao<Dict,String> {

    /**
     * 排序获取全部
     * @return
     */
    @Query(value = "select d from Dict d order by d.sortOrder")
    List<Dict> findAllOrderBySortOrder();

    /**
     * 通过type获取
     * @param type
     * @return
     */
    Dict findByType(String type);

    /**
     * 模糊搜索
     * @param key
     * @return
     */
    @Query(value = "select d from Dict d where d.title like %:key% or d.type like %:key% order by d.sortOrder")
    List<Dict> findByTitleOrTypeLike(@Param("key") String key);
}
