package com.lanyu.jenkins.hellojenkins.module.base.service.impl;

import cn.hutool.core.util.StrUtil;
import com.lanyu.jenkins.hellojenkins.module.base.dao.RoleDao;
import com.lanyu.jenkins.hellojenkins.module.base.entity.Role;
import com.lanyu.jenkins.hellojenkins.module.base.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * 角色接口实现类
 *
 * @author lanyu
 * @date 2021年06月16日 16:12
 */
@Slf4j
@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;

    /**
     * 初始化dao
     *
     * @return
     */
    @Override
    public RoleDao getRepository() {
        return roleDao;
    }

    /**
     * 获取默认角色
     *
     * @param defaultRole
     * @return
     */
    @Override
    public List<Role> findByDefaultRole(Boolean defaultRole) {
        return roleDao.findByDefaultRole(defaultRole);
    }

    /**
     * 分页获取
     * @param key
     * @param pageable
     * @return
     */
    @Override
    public Page<Role> findByCondition(String key, Pageable pageable) {
        return roleDao.findAll(new Specification<Role>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<Role> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Path<String> nameField = root.get("name");
                Path<String> descriptionField = root.get("description");
                List<Predicate> list = new ArrayList<>();
                //模糊搜索
                if (StrUtil.isNotBlank(key)){
                    Predicate p1 = criteriaBuilder.like(nameField,'%'+key+'%');
                    Predicate p2 = criteriaBuilder.like(descriptionField,'%'+key+'%');
                    list.add(criteriaBuilder.or(p1,p2));
                }
                Predicate[] arr = new Predicate[list.size()];
                criteriaQuery.where(list.toArray(arr));
                return null;
            }
        }, pageable);
    }
}
