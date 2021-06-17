package com.lanyu.jenkins.hellojenkins.module.base.service.impl;

import cn.hutool.core.util.StrUtil;
import com.lanyu.jenkins.hellojenkins.common.constant.CommonConstant;
import com.lanyu.jenkins.hellojenkins.module.base.dao.DictDataDao;
import com.lanyu.jenkins.hellojenkins.module.base.entity.DictData;
import com.lanyu.jenkins.hellojenkins.module.base.service.DictDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * 字典数据service实现类
 * @author lanyu
 * @date 2021年06月17日 10:27
 */
@Slf4j
@Service
@Transactional
public class DictDataServiceImpl implements DictDataService {

    @Autowired
    private DictDataDao dictDataDao;

    /**
     * 初始化dao
     * @return
     */
    @Override
    public DictDataDao getRepository() {
        return dictDataDao;
    }

    /**
     * 多条件获取
     * @param dictData
     * @param pageable
     * @return
     */
    @Override
    public Page<DictData> findByCondition(DictData dictData, Pageable pageable) {
        return dictDataDao.findAll(new Specification<DictData>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<DictData> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {

                Path<String> titleField = root.get("title");
                Path<Integer> statusField = root.get("status");
                Path<String> dictIdField = root.get("dictId");

                List<Predicate> list = new ArrayList<>();

                // 模糊搜素
                if (StrUtil.isNotBlank(dictData.getTitle())) {
                    list.add(cb.like(titleField, '%' + dictData.getTitle() + '%'));
                }

                // 状态
                if (dictData.getStatus() != null) {
                    list.add(cb.equal(statusField, dictData.getStatus()));
                }

                // 所属字典
                if (StrUtil.isNotBlank(dictData.getDictId())) {
                    list.add(cb.equal(dictIdField, dictData.getDictId()));
                }

                Predicate[] arr = new Predicate[list.size()];
                cq.where(list.toArray(arr));
                return null;
            }
        }, pageable);
    }

    /**
     * 通过dictId获取启用字典 已排序
     * @param dictId
     * @return
     */
    @Override
    public List<DictData> findByDictId(String dictId) {
        return dictDataDao.findByDictIdAndStatusOrderBySortOrder(dictId, CommonConstant.STATUS_NORMAL);
    }

    /**
     * 通过dictId删除
     * @param dictId
     */
    @Override
    public void deleteByDictId(String dictId) {
        dictDataDao.deleteByDictId(dictId);
    }
}
