package com.lanyu.jenkins.hellojenkins.module.base.service;

import com.lanyu.jenkins.hellojenkins.base.LanXiBaseService;
import com.lanyu.jenkins.hellojenkins.module.base.entity.DictData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 字典数据service
 * @author lanyu
 * @date 2021年06月17日 10:24
 */
public interface DictDataService  extends LanXiBaseService<DictData,String> {

    /**
     * 多条件获取
     * @param dictData
     * @param pageable
     * @return
     */
    Page<DictData> findByCondition(DictData dictData, Pageable pageable);

    /**
     * 通过dictId获取启用字典 已排序
     * @param dictId
     * @return
     */
    List<DictData> findByDictId(String dictId);

    /**
     * 通过dictId删除
     * @param dictId
     */
    void deleteByDictId(String dictId);
}
