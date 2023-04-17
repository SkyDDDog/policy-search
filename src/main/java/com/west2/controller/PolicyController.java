package com.west2.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.west2.common.CommonResult;
import com.west2.entity.vo.PolicyCollectVO;
import com.west2.entity.vo.PolicyVO;
import com.west2.service.CacheService;
import com.west2.service.PolicyPythonService;
import com.west2.util.BeanCustomUtil;
import com.west2.util.JwtTokenUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@CrossOrigin("*")
@RestController
@RequestMapping(value = "${apiPath}/policy")
@Api(value = "PolicyController", tags = "政策接口")
public class PolicyController {

    @Autowired
    private PolicyPythonService policyPythonService;
    @Autowired
    private CacheService cacheService;

    @ApiOperation(value = "查询政策")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "content",
                    value = "搜索关键词",
                    required = true,
                    dataType = "String",
                    paramType = "path",
                    example = "福建"
            ),
            @ApiImplicitParam(
                    name = "province",
                    value = "省份(eg.福建省)",
                    required = false,
                    dataType = "String",
                    paramType = "query",
                    example = "福建省"
            ),
            @ApiImplicitParam(
                    name = "grade",
                    value = "级别(eg.省级)",
                    required = false,
                    dataType = "String",
                    paramType = "query",
                    example = "省级"
            ),
            @ApiImplicitParam(
                    name = "start",
                    value = "起始日期(eg.2020-01-01)",
                    required = false,
                    dataType = "String",
                    paramType = "query",
                    example = "2020-01-01"
            ),
            @ApiImplicitParam(
                    name = "end",
                    value = "结束日期(eg.2021-01-01)",
                    required = false,
                    dataType = "String",
                    paramType = "query",
                    example = "2021-01-01"
            ),
            @ApiImplicitParam(
                    name = "pageNum",
                    value = "页码(1开始)",
                    required = false,
                    dataType = "Integer",
                    paramType = "query",
                    example = "1",
                    defaultValue = "1"
            ),
            @ApiImplicitParam(
                    name = "pageSize",
                    value = "每页数量",
                    required = false,
                    dataType = "Integer",
                    paramType = "query",
                    example = "50",
                    defaultValue = "50"
            )
    })
    @RequestMapping(value = "search/{content}", method = RequestMethod.GET)
    public CommonResult searchPolicy(@PathVariable String content,
                                     @RequestParam(required = false) String grade,
                                     @RequestParam(required = false) String province,
                                     @RequestParam(required = false) String start,
                                     @RequestParam(required = false) String end,
                                     @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                     @RequestParam(required = false, defaultValue = "50") Integer pageSize) {
        CommonResult result = new CommonResult().init();
        JSONObject jsonData = policyPythonService.searchPolicyJson(content, pageNum, pageSize, grade, province, start, end);
        List<PolicyVO> policy;
        int total = 0;
        if (jsonData!=null) {
            total = jsonData.getIntValue("total");
            JSONArray array = jsonData.getJSONArray("result");
            policy = PolicyVO.convert2PolicyVOList(array);
        } else {
            policy = new ArrayList<PolicyVO>();
        }
        String userId = JwtTokenUtil.getIdFromContext();
        List<PolicyCollectVO> vo = policyPythonService.buildCollectVOList(policy, userId);
        cacheService.incUserSearch(content);
        result.success("policy", vo);
        result.putItem("total", total);

        return (CommonResult) result.end();
    }

    @ApiOperation(value = "获取政策详情")
    @ApiImplicitParams(
            @ApiImplicitParam(
                    name = "id",
                    value = "政策id",
                    required = true,
                    dataType = "String",
                    paramType = "path"
            )
    )
    @RequestMapping(value = "detail/{id}", method = RequestMethod.GET)
    public CommonResult getDetailPolicy(@PathVariable String id) {
        CommonResult result = new CommonResult().init();
//        Policy data = policyService.getDetailPolicy(id);
        PolicyVO data = policyPythonService.getPolicyFirstById(id);
        if (data == null) {
            result.failCustom("该政策不存在");
            return (CommonResult) result.end();
        }
//        if (0 >= policyReadService.incReadCount(id)) {
//            log.info("政策{}, 阅读数增加失败", id);
//        }
        String userId = JwtTokenUtil.getIdFromContext();
        PolicyCollectVO vo;
        if (StringUtils.isNotBlank(userId)) {
            vo = policyPythonService.buildCollectVO(data, userId);
        } else {
            vo = new PolicyCollectVO();
            BeanCustomUtil.copyProperties(data, vo);
        }
        result.success("policy", vo);


        return (CommonResult) result.end();
    }



}
