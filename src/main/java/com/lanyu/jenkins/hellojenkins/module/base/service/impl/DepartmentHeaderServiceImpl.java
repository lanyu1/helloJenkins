package com.lanyu.jenkins.hellojenkins.module.base.service.impl;

import com.lanyu.jenkins.hellojenkins.common.vo.UserVo;
import com.lanyu.jenkins.hellojenkins.module.base.dao.DepartmentHeaderDao;
import com.lanyu.jenkins.hellojenkins.module.base.dao.UserDao;
import com.lanyu.jenkins.hellojenkins.module.base.entity.DepartmentHeader;
import com.lanyu.jenkins.hellojenkins.module.base.entity.User;
import com.lanyu.jenkins.hellojenkins.module.base.service.DepartmentHeaderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * 部门负责人接口实现
 * @author lanyu
 * @date 2021年06月17日 9:46
 */
@Slf4j
@Service
@Transactional
public class DepartmentHeaderServiceImpl implements DepartmentHeaderService {

    @Autowired
    private DepartmentHeaderDao departmentHeaderDao;

    @Autowired
    private UserDao userDao;

    /**
     * 初始化dao
     * @return
     */
    @Override
    public DepartmentHeaderDao getRepository() {
        return departmentHeaderDao;
    }

    /**
     * 通过部门和负责人类型获取
     * @param departmentId
     * @param type
     * @return
     */
    @Override
    public List<UserVo> findHeaderByDepartmentId(String departmentId, Integer type) {
        List<UserVo> list = new ArrayList<>();
        List<DepartmentHeader> headers = departmentHeaderDao.findByDepartmentIdAndType(departmentId,type);
        headers.forEach(e->{
            User user = userDao.getOne(e.getUserId());
            if (user != null){
                list.add(new UserVo().setId(user.getId()).setUsername(user.getUsername()).setNickname(user.getNickname()));
            }
        });
        return list;
    }

    /**
     * 通过部门获取
     * @param departmentIds
     * @return
     */
    @Override
    public List<DepartmentHeader> findByDepartmentIdIn(List<String> departmentIds) {
        return departmentHeaderDao.findByDepartmentIdIn(departmentIds);
    }

    /**
     * 通过部门id删除
     * @param departmentId
     */
    @Override
    public void deleteByDepartmentId(String departmentId) {
        departmentHeaderDao.deleteByDepartmentId(departmentId);
    }

    /**
     * 通过userId删除
     * @param userId
     */
    @Override
    public void deleteByUserId(String userId) {
        departmentHeaderDao.deleteByUserId(userId);
    }

    /**
     * 是否为部门负责人
     * @param userId
     * @param departmentId
     * @return
     */
    @Override
    public Boolean isDepartmentHeader(String userId, String departmentId) {
        List<DepartmentHeader> headers = departmentHeaderDao.findByUserIdAndDepartmentId(userId,departmentId);
        if (headers != null && !headers.isEmpty()){
            return true;
        }
        return false;
    }
}
