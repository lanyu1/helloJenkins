package com.lanyu.jenkins.hellojenkins.module.base.dao;

import com.lanyu.jenkins.hellojenkins.base.LanXiBaseDao;
import com.lanyu.jenkins.hellojenkins.module.base.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 用户数据处理层
 * @author lanyu
 * @date 2021年06月15日 13:43
 */
public interface UserDao extends LanXiBaseDao<User,String> {

    /**
     * 通过用户名获取用户
     * @param userName
     * @return
     */
    User findByUserName(String userName);

    /**
     * 通过手机获取用户
     * @param mobile
     * @return
     */
    User findByMobile(String mobile);

    /**
     * 通过邮件获取用户
     * @param email
     * @return
     */
    User findByEmail(String email);

    /**
     * 通过部门id获取用户集合
     * @param departmentId
     * @return
     */
    List<User> findByDepartmentId(String departmentId);

    /**
     * 更新部门名称
     * @param departmentId
     * @param departmentTitle
     */
    @Modifying
    @Query(" update User u set u.departmentTitle=?2 where u.departmentId=?1")
    void updateDepartmentTitle(String departmentId, String departmentTitle);
}
