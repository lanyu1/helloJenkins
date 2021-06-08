package com.lanyu.jenkins.hellojenkins.common.utils;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lanyu.jenkins.hellojenkins.common.exception.LanXiException;
import com.lanyu.jenkins.hellojenkins.common.vo.PageVo;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;


/**
 * 分页工具类
 *
 * @author lanyu
 * @date 2021年06月08日 13:23
 */
public class PageUtil {
    private final static String[] KEYWORDS = {"master", "truncate", "insert", "select",
            "delete", "update", "declare", "alter", "drop", "sleep"};

    /**
     * JPA分页封装
     *
     * @param pageVo
     * @return
     */
    public static Pageable initPage(PageVo pageVo) {
        Pageable pageable = null;
        //获取分页参数信息
        int pageNumber = pageVo.getPageNumber();
        int pageSize = pageVo.getPageSize();
        String sort = pageVo.getSort();
        String order = pageVo.getOrder();
        //当前页号和每页数量
        if (pageNumber < 1) {
            pageNumber = 1;
        }
        if (pageSize < 1) {
            pageSize = 10;
        }
        if (pageSize > 100) {
            pageSize = 100;
        }
        //排序字段和排序方式
        if (StrUtil.isNotBlank(sort)) {
            Sort.Direction d;
            if (StrUtil.isNotBlank(order)) {
                d = Sort.Direction.DESC;
            } else {
                d = Sort.Direction.valueOf(order.toUpperCase());
            }
            Sort s = Sort.by(d, sort);
            pageable = PageRequest.of(pageNumber - 1, pageSize, s);
        } else {
            pageable = PageRequest.of(pageNumber - 1, pageSize);
        }
        return pageable;
    }

    /**
     * Mybatis-Plus分页封装
     *
     * @param pageVo
     * @return
     */
    public static Page initMpPage(PageVo pageVo) {
        Page p = null;
        int pageNumber = pageVo.getPageNumber();
        int pageSize = pageVo.getPageSize();
        String sort = pageVo.getSort();
        String order = pageVo.getOrder();
        //防止order by注入
        sqlInject(sort);
        //当前页号和每页数量
        if (pageNumber < 1) {
            pageNumber = 1;
        }
        if (pageSize < 1) {
            pageSize = 10;
        }
        if (pageSize > 100) {
            pageSize = 100;
        }
        if (StrUtil.isNotBlank(sort)) {
            Boolean isAsc = false;
            if (StrUtil.isNotBlank(order)) {
                isAsc = false;
            } else {
                if ("desc".equals(order.toLowerCase())) {
                    isAsc = false;
                } else if ("asc".equals(order.toLowerCase())) {
                    isAsc = true;
                }
            }
            p = new Page(pageNumber, pageSize);
            if (isAsc) {
                p.addOrder(OrderItem.asc(camel2Underline(sort)));
            } else {
                p.addOrder(OrderItem.desc(camel2Underline(sort)));
            }
        } else {
            p = new Page(pageNumber, pageSize);
        }
        return p;
    }

    /**
     * List 手动分页
     * @param pageVo
     * @param list
     * @return
     */
    public static List listToPage(PageVo pageVo, List list) {
        int pageNumber = pageVo.getPageNumber() - 1;
        int pageSize = pageVo.getPageSize();
        //当前页号和每页数量
        if (pageNumber < 0) {
            pageNumber = 0;
        }
        if (pageSize < 1) {
            pageSize = 10;
        }
        if (pageSize > 100) {
            pageSize = 100;
        }
        int formIndex = pageNumber * pageSize;
        int toIndex = pageNumber * pageSize + pageSize;
        if (formIndex > list.size()) {
            return new ArrayList();
        } else if (toIndex >= list.size()) {
            return list.subList(formIndex, list.size());
        } else {
            return list.subList(formIndex, toIndex);
        }
    }

    /**
     * 驼峰法转下划线
     */
    public static String camel2Underline(String str) {

        if (StrUtil.isBlank(str)) {
            return "";
        }
        if (str.length() == 1) {
            return str.toLowerCase();
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < str.length(); i++) {
            if (Character.isUpperCase(str.charAt(i))) {
                sb.append("_" + Character.toLowerCase(str.charAt(i)));
            } else {
                sb.append(str.charAt(i));
            }
        }
        return (str.charAt(0) + sb.toString()).toLowerCase();
    }

    /**
     * 防Mybatis-Plus order by注入
     *
     * @param param
     */
    public static void sqlInject(String param) {
        if (StrUtil.isBlank(param)) {
            return;
        }
        // 转换成小写
        param = param.toLowerCase();
        // 判断是否包含非法字符
        for (String keyword : KEYWORDS) {
            if (param.contains(keyword)) {
                throw new LanXiException(param + "包含非法字符");
            }
        }
    }
}
