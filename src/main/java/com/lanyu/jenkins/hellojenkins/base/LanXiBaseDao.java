package com.lanyu.jenkins.hellojenkins.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

/**
 * 基础dao层接口
 * @author lanyu
 * @date 2021年06月08日 11:08
 */
//自定义接口 不会创建接口的实例 必须加此注解
@NoRepositoryBean
public interface LanXiBaseDao<E, ID extends Serializable> extends JpaRepository<E, ID>, JpaSpecificationExecutor<E> {
}
