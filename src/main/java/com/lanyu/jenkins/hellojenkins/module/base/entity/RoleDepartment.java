package com.lanyu.jenkins.hellojenkins.module.base.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.lanyu.jenkins.hellojenkins.base.LanXiBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 角色部门
 * @author lanyu
 * @date 2021年06月11日 14:03
 */
@Data
@Accessors(chain = true)
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "t_role_department")
@TableName("t_role_department")
@ApiModel(value = "角色部门")
public class RoleDepartment extends LanXiBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "角色id")
    private String roleId;

    @ApiModelProperty(value = "部门id")
    private String departmentId;
}
