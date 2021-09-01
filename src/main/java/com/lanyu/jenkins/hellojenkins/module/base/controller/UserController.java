package com.lanyu.jenkins.hellojenkins.module.base.controller;

import cn.hutool.core.util.StrUtil;
import com.lanyu.jenkins.hellojenkins.common.constant.CommonConstant;
import com.lanyu.jenkins.hellojenkins.common.exception.LanXiException;
import com.lanyu.jenkins.hellojenkins.common.redis.RedisTemplateHelper;
import com.lanyu.jenkins.hellojenkins.common.utils.PageUtil;
import com.lanyu.jenkins.hellojenkins.common.utils.ResultUtil;
import com.lanyu.jenkins.hellojenkins.common.utils.SecurityUtil;
import com.lanyu.jenkins.hellojenkins.common.vo.PageVo;
import com.lanyu.jenkins.hellojenkins.common.vo.Result;
import com.lanyu.jenkins.hellojenkins.common.vo.SearchVo;
import com.lanyu.jenkins.hellojenkins.module.base.entity.Department;
import com.lanyu.jenkins.hellojenkins.module.base.entity.Role;
import com.lanyu.jenkins.hellojenkins.module.base.entity.User;
import com.lanyu.jenkins.hellojenkins.module.base.entity.UserRole;
import com.lanyu.jenkins.hellojenkins.module.base.service.*;
import com.lanyu.jenkins.hellojenkins.module.base.vo.RoleDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户控制层
 * @author lanyu
 * @date 2021年06月18日 15:57
 */
@Slf4j
@RestController
@Api(tags = "用户接口")
@RequestMapping("/lanxi/user")
@CacheConfig(cacheNames = "user")
@Transactional
public class UserController {

    private static final String USER = "user::";

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private DepartmentHeaderService departmentHeaderService;

    @Autowired
    private IUserRoleService iUserRoleService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RedisTemplateHelper redisTemplate;

    @Autowired
    private SecurityUtil securityUtil;

    @PersistenceContext
    private EntityManager entityManager;


    /**
     * 注册用户信息
     * @param user
     * @return
     */
    @RequestMapping(value = "/register",method = RequestMethod.POST)
    @ApiOperation(value = "注册用户")
    public Result<Object> register(@Valid User user){
        //校验用户是否已存在
        checkUserInfo(user.getUsername(),user.getMobile(),user.getEmail());
        String encryptPass = new BCryptPasswordEncoder().encode(user.getPassword());
        user.setPassword(encryptPass).setType(CommonConstant.USER_TYPE_NORMAL);
        User user1 = userService.save(user);

        //默认角色
        List<Role> roleList = roleService.findByDefaultRole(true);
        if (roleList != null && roleList.size() > 0){
            roleList.forEach(r->{
                UserRole userRole = new UserRole().setUserId(user1.getId()).setRoleId(r.getId());
                userRoleService.save(userRole);
            });
        }
        return ResultUtil.data(user1);
    }


    /**
     * 获取当前登录用户接口
     * @return
     */

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    @ApiOperation(value = "获取当前登录用户接口")
    public Result<User> getUserInfo() {
        User u = securityUtil.getCurrentUser();
        // 清除持久上下文环境 避免后面语句导致持久化
        entityManager.clear();
        u.setPassword(null);
        return new ResultUtil<User>().setData(u);
    }


    /**
     * 修改绑定手机号
     * @param mobile
     * @return
     */
    @RequestMapping(value = "/changeMobile",method = RequestMethod.POST)
    @ApiOperation(value = "修改绑定手机号")
    public Result<Object> changeMobile(@RequestParam String mobile){
        User user = securityUtil.getCurrentUser();
        user.setMobile(mobile);
        userService.update(user);
        //删除缓存
        redisTemplate.delete(USER+user.getUsername());
        return ResultUtil.success("修改手机号成功");
    }

    /**
     * 解锁验证密码
     * @param password
     * @return
     */
    @RequestMapping(value = "/unlock",method = RequestMethod.POST)
    @ApiOperation(value = "解锁验证密码")
    public Result<Object> unlock(@RequestParam String password){
        User user = securityUtil.getCurrentUser();
        if (!new BCryptPasswordEncoder().matches(password, user.getPassword())){
            return ResultUtil.error("密码不正确");
        }
        return ResultUtil.data(null);
    }

    /**
     * 重置密码
     * @param ids
     * @return
     */
    @RequestMapping(value = "/resetPassword",method = RequestMethod.POST)
    @ApiOperation(value = "重置密码")
    public Result<Object> resetPassword(@RequestParam String[] ids){
        for (String id : ids){
            User user = userService.get(id);
            user.setPassword(new BCryptPasswordEncoder().encode("123456"));
            userService.update(user);
            redisTemplate.delete(USER+user.getUsername());
        }
        return ResultUtil.success("重置密码操作成功");
    }

    /**
     * 修改用户自己资料
     * @param user
     * @return
     */
    @RequestMapping(value = "/editOwn",method = RequestMethod.POST)
    @ApiOperation(value = "修改用户自己资料",notes = "用户名密码等不会修改 需要username更新缓存")
    public Result<Object> editOwn(User user){
        User oldUser = securityUtil.getCurrentUser();
        //不能修改的字段
        user.setUsername(oldUser.getUsername()).setPassword(oldUser.getPassword()).setType(user.getType()).setStatus(oldUser.getStatus());
        if (StrUtil.isBlank(user.getDepartmentId())){
            user.setDepartmentId(null);
        }
        userService.update(user);
        return ResultUtil.success("修改用户自己信息成功");
    }

    /**
     * 修改密码
     * @param oldPass
     * @param newPass
     * @param passStrength
     * @return
     */
    @RequestMapping(value = "/modifyPassword",method = RequestMethod.POST)
    @ApiOperation(value = "修改密码")
    public Result<Object> modifyPassword(@ApiParam("旧密码") @RequestParam String oldPass,
                                         @ApiParam("新密码") @RequestParam String newPass,
                                         @ApiParam("密码强度") @RequestParam String passStrength){
        User user = securityUtil.getCurrentUser();
        if (!new BCryptPasswordEncoder().matches(oldPass,user.getPassword())){
            return ResultUtil.error("旧密码不正确");
        }
        String newEncryptPass = new BCryptPasswordEncoder().encode(newPass);
        user.setPassword(newEncryptPass);
        user.setPassStrength(passStrength);
        userService.update(user);
        //手动更新缓存
        redisTemplate.delete(USER+user.getUsername());
        return ResultUtil.success("修改密码成功");
    }

    /**
     * 多条件分页获取用户列表
     * @param user
     * @param searchVo
     * @param pageVo
     * @return
     */
    @RequestMapping(value = "/getUserByCondition",method = RequestMethod.GET)
    @ApiOperation(value = "多条件分页获取用户列表")
    public Result<Page<User>> getUserByCondition(User user, SearchVo searchVo, PageVo pageVo){
        Page<User> page = userService.findByCondition(user,searchVo, PageUtil.initPage(pageVo));
        for (User u :page.getContent()){
            //关联角色
            List<Role> roleList = iUserRoleService.findByUserId(u.getId());
            List<RoleDTO> roleDTOList = roleList.stream().map(e->{
                return new RoleDTO().setId(e.getId()).setName(e.getName()).setDescription(e.getDescription());
            }).collect(Collectors.toList());
            u.setRoles(roleDTOList);
            //游离态，避免后面语句导致持久化
            entityManager.detach(u);
        }
        return new ResultUtil<Page<User>>().setData(page);
    }

    /**
     * 多条件分页获取用户列表
     * @param departmentId
     * @return
     */
    @RequestMapping(value = "/getByDepartmentId/{departmentId}", method = RequestMethod.GET)
    @ApiOperation(value = "多条件分页获取用户列表")
    public Result<List<User>> getByCondition(@PathVariable String departmentId) {

        List<User> list = userService.findByDepartmentId(departmentId);
        entityManager.clear();
        list.forEach(u -> {
            u.setPassword(null);
        });
        return new ResultUtil<List<User>>().setData(list);
    }

    /**
     * 获取全部用户数据
     * @return
     */
    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部用户数据")
    public Result<List<User>> getAll() {

        List<User> list = userService.getAll();
        // 清除持久上下文环境 避免后面语句导致持久化
        entityManager.clear();
        for (User u : list) {
            u.setPassword(null);
        }
        return new ResultUtil<List<User>>().setData(list);
    }

    /**
     * 新增用户
     * @param u
     * @param roleIds
     * @return
     */
    @RequestMapping(value = "/admin/addUser", method = RequestMethod.POST)
    @ApiOperation(value = "新增用户")
    public Result<Object> addUser(@Valid User u,@RequestParam(required = false) String[] roleIds){
        //校验用户是否已存在
        checkUserInfo(u.getUsername(),u.getMobile(),u.getEmail());
        String encryptPass = new BCryptPasswordEncoder().encode(u.getPassword());
        u.setPassStrength(encryptPass);
        if (StrUtil.isNotBlank(u.getDepartmentId())){
            Department dept = departmentService.get(u.getDepartmentId());
            if (dept != null){
                u.setDepartmentId(dept.getTitle());
            }
        }else {
            u.setDepartmentId(null);
            u.setDepartmentTitle("");
        }
        User user = userService.save(u);
        if (roleIds != null){
            //添加角色
            List<UserRole> userRoles = Arrays.asList(roleIds).stream().map(e->{
                return new UserRole().setUserId(u.getId()).setRoleId(e);
            }).collect(Collectors.toList());
            userRoleService.saveOrUpdateAll(userRoles);
        }
        return ResultUtil.success("新增用户操作成功");
    }

    /**
     * 管理员修改资料
     * @param u
     * @param roleIds
     * @return
     */
    @RequestMapping(value = "/admin/editUser", method = RequestMethod.POST)
    @ApiOperation(value = "管理员修改资料", notes = "需要通过id获取原用户信息 需要username更新缓存")
    @CacheEvict(key = "#u.username")
    public Result<Object> editUser(User u,@RequestParam(required = false) String[] roleIds){
        User oldUser = userService.get(u.getId());
        u.setUsername(oldUser.getUsername());
        //若修改了手机和邮箱判断是否唯一
        if (!oldUser.getMobile().equals(u.getMobile()) && userService.findByMobile(u.getMobile()) != null){
            return ResultUtil.error("该手机号已绑定其他账户");
        }
        if (!oldUser.getEmail().equals(u.getEmail()) && userService.findByEmail(u.getEmail()) != null){
            return ResultUtil.error("该邮箱已绑定其他账户");
        }
        if (StrUtil.isNotBlank(u.getDepartmentId())){
            Department dept = departmentService.get(u.getDepartmentId());
            if (dept != null){
                u.setDepartmentTitle(dept.getTitle());
            }
        }else {
            u.setDepartmentId(null);
            u.setDepartmentTitle("");
        }
        u.setPassword(oldUser.getPassword());
        userService.update(u);
        //删除该用户角色
        userRoleService.deleteByUserId(u.getId());
        if (roleIds != null){
            //新角色
            List<UserRole> userRoles = Arrays.asList(roleIds).stream().map(e->{
                return new UserRole().setRoleId(e).setUserId(u.getId());
            }).collect(Collectors.toList());
            userRoleService.saveOrUpdateAll(userRoles);
        }
        //手动删除缓存
        redisTemplate.delete("userRole::"+u.getId());
        redisTemplate.delete("userRole::depIds:" + u.getId());
        redisTemplate.delete("permission::userMenuList:" + u.getId());
        return ResultUtil.success("修改用户信息成功");
    }

    /**
     * 后台禁用用户
     * @param userId
     * @return
     */
    @RequestMapping(value = "/admin/disable/{userId}", method = RequestMethod.POST)
    @ApiOperation(value = "后台禁用用户")
    public Result<Object> disable(@ApiParam("用户唯一id标识") @PathVariable String userId) {

        User user = userService.get(userId);
        user.setStatus(CommonConstant.USER_STATUS_LOCK);
        userService.update(user);
        // 手动更新缓存
        redisTemplate.delete(USER + user.getUsername());
        return ResultUtil.success("操作成功");
    }

    /**
     * 后台启用用户
     * @param userId
     * @return
     */
    @RequestMapping(value = "/admin/enable/{userId}", method = RequestMethod.POST)
    @ApiOperation(value = "后台启用用户")
    public Result<Object> enable(@ApiParam("用户唯一id标识") @PathVariable String userId) {

        User user = userService.get(userId);
        user.setStatus(CommonConstant.USER_STATUS_NORMAL);
        userService.update(user);
        // 手动更新缓存
        redisTemplate.delete(USER + user.getUsername());
        return ResultUtil.success("操作成功");
    }

    /**
     * 批量通过ids删除
     * @param ids
     * @return
     */
    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过ids删除")
    public Result<Object> delAllByIds(@RequestParam String[] ids) {
        for (String id : ids) {
            User u = userService.get(id);
            // 删除相关缓存
            redisTemplate.delete(USER + u.getUsername());
            redisTemplate.delete("userRole::" + u.getId());
            redisTemplate.delete("userRole::depIds:" + u.getId());
            redisTemplate.delete("permission::userMenuList:" + u.getId());
            redisTemplate.deleteByPattern("department::*");
            //删除用户信息
            userService.delete(id);
            // 删除关联角色
            userRoleService.deleteByUserId(id);
            // 删除关联部门负责人
            departmentHeaderService.deleteByUserId(id);
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }

    /**
     * 校验
     * @param username 用户名 不校验传空字符或null 下同
     * @param mobile 手机号
     * @param email 邮箱
     */
    public void checkUserInfo(String username,String mobile,String email){
        if (StrUtil.isNotBlank(username) && userService.findByUsername(username) != null){
            throw new LanXiException("该账号已经被注册");
        }
        if (StrUtil.isNotBlank(email) && userService.findByEmail(email) != null){
            throw new LanXiException("该邮箱已经被注册");
        }
        if (StrUtil.isNotBlank(mobile) && userService.findByMobile(mobile) != null){
            throw new LanXiException("该手机号已经被注册");
        }
    }

}
