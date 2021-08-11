package com.lanyu.jenkins.hellojenkins.module.base.controller.common;

import com.lanyu.jenkins.hellojenkins.common.redis.RedisTemplateHelper;
import com.lanyu.jenkins.hellojenkins.common.utils.CreateVerifyCode;
import com.lanyu.jenkins.hellojenkins.common.utils.ResultUtil;
import com.lanyu.jenkins.hellojenkins.common.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 验证码接口
 * @author lanyu
 * @date 2021年08月11日 11:05
 */
@Api(tags = "验证码接口")
@RequestMapping("/lanxi/common/captcha")
@RestController
@Transactional
public class CaptchaController {
    @Autowired
    private RedisTemplateHelper redisTemplate;

    @RequestMapping(value = "/init", method = RequestMethod.GET)
    @ApiOperation(value = "初始化验证码")
    public Result<Object> initCaptcha() {

        String captchaId = UUID.randomUUID().toString().replace("-", "");
        String code = new CreateVerifyCode().randomStr(4);
        // 缓存验证码
        redisTemplate.set(captchaId, code, 2L, TimeUnit.MINUTES);
        return ResultUtil.data(captchaId);
    }

    @RequestMapping(value = "/draw/{captchaId}", method = RequestMethod.GET)
    @ApiOperation(value = "根据验证码ID获取图片")
    public void drawCaptcha(@PathVariable("captchaId") String captchaId, HttpServletResponse response) throws IOException {

        // 得到验证码 生成指定验证码
        String code = redisTemplate.get(captchaId);
        CreateVerifyCode vCode = new CreateVerifyCode(116, 36, 4, 10, code);
        response.setContentType("image/png");
        vCode.write(response.getOutputStream());
    }
}
