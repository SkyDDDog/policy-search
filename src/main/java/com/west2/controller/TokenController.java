package com.west2.controller;

import com.west2.common.CommonResult;
import com.west2.common.MsgCodeUtil;
import com.west2.config.JwtProperties;
import com.west2.util.JwtTokenUtil;
import com.west2.util.RedisUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin("*")
@RestController
@RequestMapping(value = "${apiPath}/token")
@Api(value = "TokenController", tags = "token接口")
public class TokenController {

    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @ApiOperation(value = "刷新token", notes = "")
    @ApiImplicitParams(
            @ApiImplicitParam(
                    name = "token",
                    value = "refresh-token",
                    paramType = "path",
                    dataType = "string",
                    required = true)
    )
    @RequestMapping(value = "refresh/{token}", method = RequestMethod.GET)
    public CommonResult refreshToken(@PathVariable String token) {
        CommonResult result = new CommonResult().init();
        if (token == null) {
            return (CommonResult) result.fail(MsgCodeUtil.MSG_CODE_JWT_TOKEN_ISNULL).end();
        }
        String refreshtoken = token.substring(jwtProperties.getTokenHead().length());
        String username = jwtTokenUtil.getUserNameFromToken(refreshtoken);
        if (username == null) {
            return (CommonResult) result.fail(MsgCodeUtil.MSG_CODE_JWT_MALFORMED).end();
        }
        String accessToken = RedisUtil.StringOps.get(jwtTokenUtil.getAccessTokenKey(username));
        String new_accessToken = jwtTokenUtil.refreshHeadToken(refreshtoken, accessToken);
        if (new_accessToken == null) {
            return (CommonResult) result.fail(MsgCodeUtil.MSG_CODE_JWT_MALFORMED).end();
        }
        if (new_accessToken == "") {
            return (CommonResult) result.fail(MsgCodeUtil.MSG_CODE_JWT_TOKEN_ISNULL).end();
        }
        RedisUtil.StringOps.set(jwtTokenUtil.getAccessTokenKey(username), new_accessToken);

        return (CommonResult) result.success("token", new_accessToken).end();
    }

}
