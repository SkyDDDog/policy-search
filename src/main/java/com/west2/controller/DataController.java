package com.west2.controller;

import com.west2.common.CommonResult;
import com.west2.common.MsgCodeUtil;
import com.west2.entity.vo.Top10VO;
import com.west2.entity.vo.WordFrequencyVO;
import com.west2.service.CacheService;
import com.west2.service.UserDetailService;
import com.west2.util.DateTimeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;


@Slf4j
@CrossOrigin("*")
@RestController
@RequestMapping(value = "${apiPath}/data")
@Api(value = "DataController", tags = "后台数据接口")
public class DataController {

    @Autowired
    private UserDetailService userDetailService;
    @Autowired
    private CacheService cacheService;

    @ApiOperation(value = "获取累计注册量")
    @RequestMapping(value = "register", method = RequestMethod.GET)
    public CommonResult getRegisterCount() {
        CommonResult result = new CommonResult().init();
        int count = userDetailService.getRegisterCount();
        result.success("count", count);
        return (CommonResult) result.end();
    }


    @ApiOperation(value = "获取访问量")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "type",
                    value = "类型(1今日/2七日内/3三十日内/4一年内)",
                    paramType = "path",
                    dataType = "int",
                    required = true,
                    defaultValue = "1"
            )
    })
    @RequestMapping(value = "access/{type}", method = RequestMethod.GET)
    public CommonResult getUserAccessData(@PathVariable int type) {
        CommonResult result = new CommonResult().init();
        int count = 0;
        if (type==1) {
            count = cacheService.getDayUserAccess();
        } else if (type==2) {
            count = cacheService.getWeekUserAccess();
        } else if (type==3) {
            count = cacheService.getMonthUserAccess();
        } else if (type==4) {
            count = cacheService.getYearUserAccess();
        } else {
            result.failCustom(MsgCodeUtil.MSG_CODE_ILLEGAL_ARGUMENT, "type参数错误");
        }

        result.success("count", count);

        return (CommonResult) result.end();
    }

    @ApiOperation(value = "获取搜索量")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "type",
                    value = "类型(1今日/2七日内/3三十日内/4一年内)",
                    paramType = "path",
                    dataType = "int",
                    required = true,
                    defaultValue = "1"
            )
    })
    @RequestMapping(value = "search/{type}", method = RequestMethod.GET)
    public CommonResult getUserSearchData(@PathVariable int type) {
        CommonResult result = new CommonResult().init();
        int count = 0;
        if (type==1) {
            count = cacheService.getDayUserSearch();
        } else if (type==2) {
            count = cacheService.getWeekUserSearch();
        } else if (type==3) {
            count = cacheService.getMonthUserSearch();
        } else if (type==4) {
            count = cacheService.getYearUserSearch();
        } else {
            result.failCustom(MsgCodeUtil.MSG_CODE_ILLEGAL_ARGUMENT, "type参数错误");
        }

        result.success("count", count);
        return (CommonResult) result.end();
    }

    @ApiOperation(value = "获取搜索关键词top10")
    @RequestMapping(value = "search/top10", method = RequestMethod.GET)
    public CommonResult getTop10Search() {
        CommonResult result = new CommonResult().init();

        List<Top10VO> top10 = cacheService.getTop10Search();
        result.success("top10", top10);

        return (CommonResult) result.end();
    }

    @ApiOperation(value = "后台图表数据")
    @RequestMapping(value = "chart", method = RequestMethod.GET)
    public CommonResult getChartData() {
        CommonResult result = new CommonResult().init();

        List<Integer> userAccessList = cacheService.getDayUserAccessList();
        List<Integer> userSearchList = cacheService.getDayUserSearchList();
        List<String> xMeasure = DateTimeUtil.getChartWeekDate();
        Collections.reverse(userAccessList);
        Collections.reverse(userSearchList);
        Collections.reverse(xMeasure);
        Integer accessMax = userAccessList.stream().max(Comparator.comparing(Integer::intValue)).get();
        Integer searchMax = userSearchList.stream().max(Comparator.comparing(Integer::intValue)).get();
        result.success("xMeasure", xMeasure);
        result.putItem("userAccess", userAccessList);
        result.putItem("userSearch", userSearchList);
        result.putItem("accessMax", accessMax);
        result.putItem("searchMax", searchMax);
        return (CommonResult) result.end();
    }

    @ApiOperation(value = "搜索词频")
    @RequestMapping(value = "search/word/frequency", method = RequestMethod.GET)
    public CommonResult getSearchWord() {
        CommonResult result = new CommonResult().init();

        List<WordFrequencyVO> wordList = cacheService.getSearchWordFrequency();
        result.success("wordList", wordList);
        return (CommonResult) result.end();
    }


}
