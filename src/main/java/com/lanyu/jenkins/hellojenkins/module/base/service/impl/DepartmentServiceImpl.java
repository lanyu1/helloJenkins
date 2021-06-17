package com.lanyu.jenkins.hellojenkins.module.base.service.impl;

import com.lanyu.jenkins.hellojenkins.base.LanXiBaseDao;
import com.lanyu.jenkins.hellojenkins.common.utils.SecurityUtil;
import com.lanyu.jenkins.hellojenkins.module.base.dao.DepartmentDao;
import com.lanyu.jenkins.hellojenkins.module.base.entity.Department;
import com.lanyu.jenkins.hellojenkins.module.base.service.DepartmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * 部门service实现类
 * @author lanyu
 * @date 2021年06月17日 9:38
 */
@Slf4j
@Service
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private SecurityUtil securityUtil;
    /**
     * 初始化dao
     * @return
     */
    @Override
    public DepartmentDao getRepository() {
        return departmentDao;
    }

    /**
     * 通过父id获取 升序
     * @param parentId
     * @param openDataFilter 是否开启数据权限
     * @return
     */
    @Override
    public List<Department> findByParentIdOrderBySortOrder(String parentId, Boolean openDataFilter) {
        List<String> deptIds = securityUtil.getDepartmentIds();
        if (deptIds != null && deptIds.size() >0 && openDataFilter){
            return departmentDao.findByParentIdAndIdInOrderBySortOrder(parentId,deptIds);
        }
        return departmentDao.findByParentIdOrderBySortOrder(parentId);
    }

    /**
     * 通过父id和状态获取
     * @param parentId
     * @param status
     * @return
     */
    @Override
    public List<Department> findByParentIdAndStatusOrderBySortOrder(String parentId, Integer status) {
        return departmentDao.findByParentIdAndStatusOrderBySortOrder(parentId,status);
    }

    /**
     * 部门名模糊搜索 升序
     * @param title
     * @param openDataFilter 是否开启数据权限
     * @return
     */
    @Override
    public List<Department> findByTitleLikeOrderBySortOrder(String title, Boolean openDataFilter) {
        List<String> deptIds = securityUtil.getDepartmentIds();
        if (deptIds != null && deptIds.size() >0 && openDataFilter){
            return departmentDao.findByTitleLikeAndIdInOrderBySortOrder(title,deptIds);
        }
        return departmentDao.findByTitleLikeOrderBySortOrder(title);
    }
}
