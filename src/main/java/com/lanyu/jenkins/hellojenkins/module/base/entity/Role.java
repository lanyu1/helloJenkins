package com.lanyu.jenkins.hellojenkins.module.base.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lanyu.jenkins.hellojenkins.base.LanXiBaseEntity;
import com.lanyu.jenkins.hellojenkins.common.constant.CommonConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

/**
 * 角色实体类
 * @author lanyu
 * @date 2021年06月11日 14:00
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "t_role")
@TableName("t_role")
@ApiModel(value = "角色")
public class Role extends LanXiBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "角色名 以ROLE_开头")
    private String name;

    @ApiModelProperty(value = "是否为注册默认角色")
    private Boolean defaultRole;

    @ApiModelProperty(value = "数据权限类型 0全部默认 1自定义")
    private Integer dataType = CommonConstant.DATA_TYPE_ALL;

    @ApiModelProperty(value = "备注")
    private String description;

    @Transient
    @TableField(exist = false)
    @ApiModelProperty(value = "拥有权限")
    private List<RolePermission> permissions;

    @Transient
    @TableField(exist = false)
    @ApiModelProperty(value = "拥有数据权限")
    private List<RoleDepartment> departments;

}
