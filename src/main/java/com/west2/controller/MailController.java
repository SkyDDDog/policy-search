package com.west2.controller;

import com.west2.common.CommonResult;
import com.west2.common.MsgCodeUtil;
import com.west2.service.MailService;
import com.west2.util.RedisUtil;
import com.west2.util.VerCodeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@Slf4j
@CrossOrigin("*")
@RestController
@RequestMapping(value = "${apiPath}/email")
@Api(value = "MailController", tags = "邮箱接口")
public class MailController {

    @Autowired
    private MailService mailService;

    @ApiOperation("给对应用户发送邮件验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "email",
                    value = "用户邮箱",
                    paramType = "path",
                    dataType = "string",
                    required = true),
    })
    @RequestMapping(value = "/vercode/send/{email}", method = RequestMethod.POST)
    public CommonResult sendEmail(@PathVariable String email) {
        CommonResult result = new CommonResult().init();
        //key 邮箱号  value 验证码
        String code = RedisUtil.StringOps.get(email);
        //从redis获取验证码，如果获取获取到，返回ok
        if (!StringUtils.isEmpty(code)) {
            System.out.println("redis中已存在验证码");
            return (CommonResult) result.fail(MsgCodeUtil.MSG_CODE_VERCODE_EXISTED).end();
        }
        //如果从redis获取不到，生成新的4位验证码
        code = VerCodeUtil.getVerCode();
        //生成验证码放到redis里面，设置有效时间
        if (mailService.sendMail(email, code)) {
            RedisUtil.StringOps.setEx(email, code, 1, TimeUnit.MINUTES);
            result.success("code", code);
        } else {
            result.fail(MsgCodeUtil.MSG_CODE_VERCODE_SEND_FAILURE);
        }
        return (CommonResult) result.end();
    }

    @ApiOperation("验证邮箱验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "email",
                    value = "用户邮箱",
                    paramType = "path",
                    dataType = "string",
                    required = true),
            @ApiImplicitParam(
                    name = "code",
                    value = "验证码",
                    paramType = "path",
                    dataType = "string",
                    required = true),
    })
    @RequestMapping(value = "/vercode/verify/{email}/{code}", method = RequestMethod.GET)
    public CommonResult compareCode(@PathVariable String email, @PathVariable String code) {
        CommonResult result = new CommonResult().init();
        String verCode = RedisUtil.StringOps.get(email);
        if (code.equals(verCode)) {
            result.success("code", code);
        } else {
            result.fail(MsgCodeUtil.MSG_CODE_VERCODE_INVALID);
        }
        return (CommonResult) result.end();
    }


}
