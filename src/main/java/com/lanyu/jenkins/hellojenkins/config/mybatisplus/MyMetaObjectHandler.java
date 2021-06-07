package com.lanyu.jenkins.hellojenkins.config.mybatisplus;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 字段填充审计
 * @author lanyu
 * @date 2021年05月27日 11:12
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    /**
     * 插入
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        setFieldUser("createBy",metaObject);
        this.setFieldValByName("createTime",new Date(),metaObject);
    }

    /**
     * 更新
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        setFieldUser("updateBy",metaObject);
        this.setFieldValByName("updateTime",new Date(),metaObject);
    }

    /**
     * 设置用户字段填充
     * @param field
     * @param metaObject
     */
    public void setFieldUser(String field, MetaObject metaObject){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getName() != null && !(authentication instanceof AnonymousAuthenticationToken)){
            this.setFieldValByName(field,authentication.getName(),metaObject);
        }
    }
}
