package com.lanyu.jenkins.hellojenkins.module.base.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.lanyu.jenkins.hellojenkins.common.utils.SecurityUtil;
import com.lanyu.jenkins.hellojenkins.common.vo.SearchVo;
import com.lanyu.jenkins.hellojenkins.module.base.dao.UserDao;
import com.lanyu.jenkins.hellojenkins.module.base.dao.mapper.PermissionMapper;
import com.lanyu.jenkins.hellojenkins.module.base.dao.mapper.UserRoleMapper;
import com.lanyu.jenkins.hellojenkins.module.base.entity.Permission;
import com.lanyu.jenkins.hellojenkins.module.base.entity.Role;
import com.lanyu.jenkins.hellojenkins.module.base.entity.User;
import com.lanyu.jenkins.hellojenkins.module.base.service.UserService;
import com.lanyu.jenkins.hellojenkins.module.base.vo.PermissionDTO;
import com.lanyu.jenkins.hellojenkins.module.base.vo.RoleDTO;
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
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户接口实现类
 *
 * @author lanyu
 * @date 2021年06月15日 15:59
 */
@Slf4j
@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    /**
     * 用户角色关联mapper
     */
    @Autowired
    private UserRoleMapper userRoleMapper;

    /**
     * 权限菜单mapper
     */
    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private SecurityUtil securityUtil;

    /**
     * 获取用户数据处理层
     *
     * @return
     */
    @Override
    public UserDao getRepository() {
        return userDao;
    }

    /**
     * 通过用户名获取用户
     *
     * @param username
     * @return
     */
    @Override
    public User findByUsername(String username) {
        User user = userDao.findByUserName(username);
        return userToDTO(user);
    }

    /**
     * 通过手机获取用户
     *
     * @param mobile
     * @return
     */
    @Override
    public User findByMobile(String mobile) {
        User user = userDao.findByMobile(mobile);
        return userToDTO(user);
    }

    /**
     * 通过邮件获取用户
     *
     * @param email
     * @return
     */
    @Override
    public User findByEmail(String email) {
        User user = userDao.findByEmail(email);
        return userToDTO(user);
    }

    /**
     * 多条件分页获取用户
     *
     * @param user
     * @param searchVo
     * @param pageable
     * @return
     */
    @Override
    public Page<User> findByCondition(User user, SearchVo searchVo, Pageable pageable) {
        return userDao.findAll(new Specification<User>() {
            @Override
            @Nullable
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Path<String> idField = root.get("id");
                Path<String> usernameField = root.get("username");
                Path<String> nikenameField = root.get("nikename");
                Path<String> mobileField = root.get("mobile");
                Path<String> emailField = root.get("email");
                Path<String> departmentIdField = root.get("departmentId");
                Path<String> sexField = root.get("sex");
                Path<String> typeField = root.get("type");
                Path<String> statusField = root.get("status");
                Path<Date> createTimeField = root.get("createTime");
                List<Predicate> list = new ArrayList<>();

                //用户id
                if (StrUtil.isNotBlank(user.getId())) {
                    list.add(criteriaBuilder.equal(idField, user.getId()));
                }
                //模糊查询
                //1.用户名
                if (StrUtil.isNotBlank(user.getUsername())) {
                    list.add(criteriaBuilder.like(usernameField, '%' + user.getUsername() + '%'));
                }
                //2.用户昵称
                if (StrUtil.isNotBlank(user.getNickname())) {
                    list.add(criteriaBuilder.like(nikenameField, '%' + user.getNickname() + '%'));
                }
                //3.手机号码
                if (StrUtil.isNotBlank(user.getMobile())) {
                    list.add(criteriaBuilder.like(mobileField, '%' + user.getMobile() + '%'));
                }
                //4.邮箱
                if (StrUtil.isNotBlank(user.getEmail())) {
                    list.add(criteriaBuilder.like(emailField, '%' + user.getEmail() + '%'));
                }
                //5.部门
                if (StrUtil.isNotBlank(user.getDepartmentId())) {
                    list.add(criteriaBuilder.equal(departmentIdField, user.getDepartmentId()));
                }
                //6.性别
                if (StrUtil.isNotBlank(user.getSex())) {
                    list.add(criteriaBuilder.equal(sexField, user.getSex()));
                }
                //7.类型
                if (user.getType() != null) {
                    list.add(criteriaBuilder.equal(typeField, user.getType()));
                }
                //8.状态
                if (user.getStatus() != null) {
                    list.add(criteriaBuilder.equal(statusField, user.getStatus()));
                }
                //9.创建时间
                if (StrUtil.isNotBlank(searchVo.getStartDate()) && StrUtil.isNotBlank(searchVo.getEndDate())) {
                    Date start = DateUtil.parse(searchVo.getStartDate());
                    Date end = DateUtil.parse(searchVo.getEndDate());
                    list.add(criteriaBuilder.between(createTimeField, start, DateUtil.endOfDay(end)));
                }
                // 数据权限
                List<String> depIds = securityUtil.getDepartmentIds();
                if (depIds != null && depIds.size() > 0) {
                    list.add(departmentIdField.in(depIds));
                }
                Predicate[] arr = new Predicate[list.size()];
                criteriaQuery.where(list.toArray(arr));
                return null;
            }
        }, pageable);
    }

    /**
     * 通过部门id获取
     *
     * @param departmentId
     * @return
     */
    @Override
    public List<User> findByDepartmentId(String departmentId) {
        return userDao.findByDepartmentId(departmentId);
    }

    /**
     * 更新部门名称
     *
     * @param departmentId
     * @param departmentTitle
     */
    @Override
    public void updateDepartmentTitle(String departmentId, String departmentTitle) {
        userDao.updateDepartmentTitle(departmentId, departmentTitle);
    }

    /**
     * 将用户角色和权限菜单进行关联处理
     *
     * @param user
     * @return
     */
    public User userToDTO(User user) {
        if (user == null) {
            return null;
        }
        //关联角色
        List<Role> roleList = userRoleMapper.findByUserId(user.getId());
        List<RoleDTO> roleDTOList = roleList.stream().map(e -> {
            return new RoleDTO().setId(e.getId()).setName(e.getName());
        }).collect(Collectors.toList());
        user.setRoles(roleDTOList);
        //关联权限菜单
        List<Permission> permissionList = permissionMapper.findByUserId(user.getId());
        List<PermissionDTO> permissionDTOList = permissionList.stream().map(e -> {
            return new PermissionDTO().setTitle(e.getTitle()).setPath(e.getPath());
        }).collect(Collectors.toList());
        user.setPermissions(permissionDTOList);
        return user;
    }

}
