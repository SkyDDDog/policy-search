package com.west2.controller;

import com.west2.common.CommonResult;
import com.west2.common.MsgCodeUtil;
import com.west2.entity.UserCollection;
import com.west2.entity.vo.PolicyCollectVO;
import com.west2.service.UserCollectionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@CrossOrigin("*")
@RestController
@RequestMapping(value = "${apiPath}/collect")
@Api(value = "CollectController", tags = "收藏接口")
public class CollectController {

    @Autowired
    private UserCollectionService userCollectionService;

    @ApiOperation(value = "收藏政策")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "userId",
                    value = "用户id",
                    required = true,
                    dataType = "String",
                    paramType = "path"
            ),
            @ApiImplicitParam(
                    name = "policyId",
                    value = "政策id",
                    required = true,
                    dataType = "String",
                    paramType = "path"
            )
    })
    @RequestMapping(value = "policy/{userId}/{policyId}", method = RequestMethod.POST)
    public CommonResult collectPolicy(@PathVariable String userId, @PathVariable String policyId) {
        CommonResult result = new CommonResult().init();
        if (userCollectionService.isCollected(userId, policyId)) {
            result.failCustom("该用户已收藏该政策");
        } else {
            if (0 < userCollectionService.collectPolicy(userId, policyId)) {
                result.success();
            } else {
                result.fail(MsgCodeUtil.MSG_CODE_UNKNOWN);
            }
            result.success();
        }

        return (CommonResult) result.end();
    }

    @ApiOperation(value = "取消收藏政策")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "userId",
                    value = "用户id",
                    required = true,
                    dataType = "String",
                    paramType = "path"
            ),
            @ApiImplicitParam(
                    name = "policyId",
                    value = "政策id",
                    required = true,
                    dataType = "String",
                    paramType = "path"
            )
    })
    @RequestMapping(value = "policy/{userId}/{policyId}", method = RequestMethod.DELETE)
    public CommonResult unCollectPolicy(@PathVariable String userId, @PathVariable String policyId) {
        CommonResult result = new CommonResult().init();
        if (!userCollectionService.isCollected(userId, policyId)) {
            result.failCustom("该用户未收藏该政策");
        } else {
            if (0 < userCollectionService.unCollectPolicy(userId, policyId)) {
                result.success();
            } else {
                result.fail(MsgCodeUtil.MSG_CODE_UNKNOWN);
            }
            result.success();
        }

        return (CommonResult) result.end();
    }

    @ApiOperation(value = "获取收藏政策")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "userId",
                    value = "用户id",
                    required = true,
                    dataType = "String",
                    paramType = "path"
            )
    })
    @RequestMapping(value = "policy/{userId}", method = RequestMethod.GET)
    public CommonResult getCollectPolicy(@PathVariable String userId) {
        CommonResult result = new CommonResult().init();
        List<PolicyCollectVO> data = userCollectionService.getCollectPolicy(userId);
        result.success("policy", data);
        result.putItem("total", data.size());
        return (CommonResult) result.end();
    }

}
