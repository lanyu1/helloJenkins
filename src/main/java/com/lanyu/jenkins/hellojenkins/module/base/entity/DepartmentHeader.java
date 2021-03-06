package com.lanyu.jenkins.hellojenkins.module.base.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.lanyu.jenkins.hellojenkins.base.LanXiBaseEntity;
import com.lanyu.jenkins.hellojenkins.common.constant.CommonConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 部门负责人实体类
 * @author lanyu
 * @date 2021年06月11日 14:09
 */
@Data
@Accessors(chain = true)
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "t_department_header")
@TableName("t_department_header")
@ApiModel(value = "部门负责人")
public class DepartmentHeader extends LanXiBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "关联部门id")
    private String departmentId;

    @ApiModelProperty(value = "关联部门负责人")
    private String userId;

    @ApiModelProperty(value = "负责人类型 默认0主要 1副职")
    private Integer type = CommonConstant.HEADER_TYPE_MAIN;
}
