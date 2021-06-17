package com.lanyu.jenkins.hellojenkins.module.base.service.impl;

import com.lanyu.jenkins.hellojenkins.base.LanXiBaseDao;
import com.lanyu.jenkins.hellojenkins.module.base.dao.DictDao;
import com.lanyu.jenkins.hellojenkins.module.base.entity.Dict;
import com.lanyu.jenkins.hellojenkins.module.base.service.DictService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * 字典service实现类
 * @author lanyu
 * @date 2021年06月17日 10:19
 */
@Slf4j
@Service
@Transactional
public class DictServiceImpl implements DictService {

    @Autowired
    private DictDao dictDao;

    /**
     * 初始化dao
     * @return
     */
    @Override
    public DictDao getRepository() {
        return dictDao;
    }

    /**
     * 排序获取全部
     * @return
     */
    @Override
    public List<Dict> findAllOrderBySortOrder() {
        return dictDao.findAllOrderBySortOrder();
    }

    /**
     * 通过type获取
     * @param type
     * @return
     */
    @Override
    public Dict findByType(String type) {
        return dictDao.findByType(type);
    }

    /**
     * 模糊搜索
     * @param key
     * @return
     */
    @Override
    public List<Dict> findByTitleOrTypeLike(String key) {
        return dictDao.findByTitleOrTypeLike(key);
    }
}
