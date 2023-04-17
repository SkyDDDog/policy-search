package com.west2.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.west2.common.CommonResult;
import com.west2.entity.vo.DistrictVO;
import com.west2.entity.vo.IndustryVO;
import com.west2.service.DistrictService;
import com.west2.service.IndustryCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@CrossOrigin("*")
@RestController
@RequestMapping(value = "${apiPath}/common")
@Api(value = "CommonController", tags = "通用接口")
public class CommonController {

    @Autowired
    private IndustryCategoryService industryCategoryService;
    @Autowired
    private DistrictService districtService;

    @ApiOperation(value = "连通性测试")
    @RequestMapping(value = "ping", method = RequestMethod.GET)
    public String registerUser() {
        return "pong";
    }

    @ApiOperation(value = "产业类别列表")
    @RequestMapping(value = "industry", method = RequestMethod.GET)
    public CommonResult getIndustryCategoryList() {
        CommonResult result = new CommonResult().init();
        List<IndustryVO> list = industryCategoryService.getIndustryCategory();

        result.success("industry_category", list);
        log.info("获取产业类别列表成功");
        return (CommonResult) result.end();
    }

    @ApiOperation(value = "所在地列表")
    @RequestMapping(value = "district", method = RequestMethod.GET)
    public CommonResult getDistrict() {
        CommonResult result = new CommonResult().init();
        List<DistrictVO> list = districtService.getDistrict();


        result.success("industry_category", list);
        log.info("获取所在地列表成功");
        return (CommonResult) result.end();
    }



}
