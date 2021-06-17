package com.lanyu.jenkins.hellojenkins.common.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 模糊查询开始结束时间Vo
 * @author lanyu
 * @date 2021年06月15日 15:58
 */
@Data
public class SearchVo implements Serializable {

    private String startDate;
    private String endDate;
}
