package com.lanyu.jenkins.hellojenkins.module.base.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 角色DTO
 * @author lanyu
 * @date 2021年06月08日 20:35
 */
@Data
@Accessors(chain = true)
public class RoleDTO {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "角色名 以ROLE_开头")
    private String name;

    @ApiModelProperty(value = "备注")
    private String description;
}
