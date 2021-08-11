package com.lanyu.jenkins.hellojenkins.common.utils;

import cn.hutool.core.bean.BeanUtil;
import com.lanyu.jenkins.hellojenkins.module.base.entity.Permission;
import com.lanyu.jenkins.hellojenkins.module.base.vo.MenuVo;

/**
 * VO工具类
 * @author lanyu
 * @date 2021年08月11日 10:15
 */
public class VoUtil {
    public static MenuVo permissionToMenuVo(Permission p) {
        MenuVo menuVo = new MenuVo();
        BeanUtil.copyProperties(p, menuVo);
        return menuVo;
    }
}
