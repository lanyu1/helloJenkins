package com.lanyu.jenkins.hellojenkins.module.base.controller.common;

import com.lanyu.jenkins.hellojenkins.common.utils.IpInfoUtil;
import com.lanyu.jenkins.hellojenkins.common.utils.ResultUtil;
import com.lanyu.jenkins.hellojenkins.common.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lanyu
 * @date 2021年08月11日 11:08
 */
@Slf4j
@RestController
@Api(tags = "IP接口")
@RequestMapping("/lanxi/common/ip")
@Transactional
public class IpInfoController {
    @Autowired
    private IpInfoUtil ipInfoUtil;

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    @ApiOperation(value = "IP及天气相关信息")
    public Result<Object> upload(HttpServletRequest request) {

        String result = ipInfoUtil.getIpCity(request);
        return ResultUtil.data(result);
    }
}
