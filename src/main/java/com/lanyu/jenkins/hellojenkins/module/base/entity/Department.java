package com.lanyu.jenkins.hellojenkins.module.base.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lanyu.jenkins.hellojenkins.base.LanXiBaseEntity;
import com.lanyu.jenkins.hellojenkins.common.constant.CommonConstant;
import com.lanyu.jenkins.hellojenkins.common.vo.UserVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.List;

/**
 * 部门实体类
 * @author lanyu
 * @date 2021年06月11日 14:07
 */
@Data
@Accessors(chain = true)
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "t_department")
@TableName("t_department")
@ApiModel(value = "部门")
public class Department extends LanXiBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "部门名称")
    private String title;

    @ApiModelProperty(value = "父id")
    @Column(nullable = false)
    private String parentId;

    @ApiModelProperty(value = "是否为父节点(含子节点) 默认false")
    private Boolean isParent = false;

    @ApiModelProperty(value = "排序值")
    @Column(precision = 10, scale = 2)
    private BigDecimal sortOrder;

    @ApiModelProperty(value = "是否启用 0启用 -1禁用")
    private Integer status = CommonConstant.STATUS_NORMAL;

    @Transient
    @TableField(exist = false)
    @ApiModelProperty(value = "父节点名称")
    private String parentTitle;

    @Transient
    @TableField(exist = false)
    @ApiModelProperty(value = "主负责人")
    private List<String> mainHeader;

    @Transient
    @TableField(exist = false)
    @ApiModelProperty(value = "副负责人")
    private List<String> viceHeader;

    @Transient
    @TableField(exist = false)
    @ApiModelProperty(value = "主负责人")
    private List<UserVo> mainHeaders;

    @Transient
    @TableField(exist = false)
    @ApiModelProperty(value = "副负责人")
    private List<UserVo> viceHeaders;
}
