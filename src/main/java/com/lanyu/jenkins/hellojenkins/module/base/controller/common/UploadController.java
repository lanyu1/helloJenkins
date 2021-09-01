package com.lanyu.jenkins.hellojenkins.module.base.controller.common;

import cn.hutool.core.util.StrUtil;
import com.lanyu.jenkins.hellojenkins.common.utils.Base64DecodeMultipartFile;
import com.lanyu.jenkins.hellojenkins.common.utils.CommonUtil;
import com.lanyu.jenkins.hellojenkins.common.utils.QiniuUtil;
import com.lanyu.jenkins.hellojenkins.common.utils.ResultUtil;
import com.lanyu.jenkins.hellojenkins.common.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * 文件上传controller
 * @author lanyu
 * @date 2021年08月11日 11:02
 */
@Slf4j
@RestController
@Api(tags = "文件上传接口")
@RequestMapping("/lanxi/upload")
@Transactional
public class UploadController {

    @Value("${lanxi.maxUploadFile}")
    private Integer maxUploadFile;

    @Autowired
    private QiniuUtil qiniuUtil;

    @RequestMapping(value = "/file", method = RequestMethod.POST)
    @ApiOperation(value = "文件上传")
    public Result<Object> upload(@RequestParam(required = false) MultipartFile file,
                                 @RequestParam(required = false) String base64) {

        if (file.getSize() > maxUploadFile * 1024 * 1024) {
            return ResultUtil.error("文件大小过大，不能超过" + maxUploadFile + "MB");
        }
        if (StrUtil.isNotBlank(base64)) {
            // base64上传
            file = Base64DecodeMultipartFile.base64Convert(base64);
        }
        String result;
        String fileName = CommonUtil.renamePic(file.getOriginalFilename());
        try {
            InputStream inputStream = file.getInputStream();
            // 上传七牛云服务器
            result = qiniuUtil.qiniuInputStreamUpload(inputStream, fileName);
        } catch (Exception e) {
            log.error(e.toString());
            return ResultUtil.error(e.toString());
        }

        return ResultUtil.data(result);
    }
}
