package com.west2.controller;

import com.west2.common.CommonResult;
import com.west2.common.MsgCodeUtil;
import com.west2.service.TestService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@CrossOrigin("*")
@RestController
@RequestMapping(value = "${apiPath}/test")
@Api(value = "TestController", tags = "测试接口")
public class TestController {

    @Autowired
    private TestService testService;

    @RequestMapping(value = "test", method = RequestMethod.GET)
    public CommonResult test() {
        CommonResult result = new CommonResult().init();
        try {
            testService.test();
            result.success();
        } catch (InterruptedException e) {
            e.printStackTrace();
            result.fail(MsgCodeUtil.MSG_CODE_UNKNOWN);
        }
        return (CommonResult) result.end();
    }

}
