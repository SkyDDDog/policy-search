package com.west2.controller;


import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.west2.common.CommonResult;
import com.west2.common.MsgCodeUtil;
import com.west2.entity.Role;
import com.west2.entity.UserDetail;
import com.west2.entity.UserInfo;
import com.west2.entity.dto.UserBasicDTO;
import com.west2.entity.dto.UserInfoDTO;
import com.west2.entity.dto.UserPasswordDTO;
import com.west2.entity.vo.UserInfoVO;
import com.west2.service.UserInfoService;
import com.west2.service.UserDetailService;
import com.west2.util.BeanCustomUtil;
import com.west2.util.JwtTokenUtil;
import com.west2.util.JwtUtil;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Slf4j
@CrossOrigin("*")
@RestController
@RequestMapping(value = "${apiPath}/user")
@Api(value = "UserController", tags = "用户接口")
public class UserController {

    @Autowired
    private UserDetailService userService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private BCryptPasswordEncoder encoder;
    @Autowired
    private UserDetailService userDetailService;

    @ApiOperation(value = "注册")
    @RequestMapping(value = "register", method = RequestMethod.POST)
    public CommonResult registerUser(@Validated @RequestBody @ApiParam("注册用户vo") UserBasicDTO vo,
                                   BindingResult bindingResult) {
        CommonResult result = new CommonResult().init();
        // 参数验证
        if (bindingResult.hasErrors()) {
            return (CommonResult) result.failIllegalArgument(bindingResult.getFieldErrors()).end();
        }
        UserDetail user = userService.getByLoginUsername(vo.getUsername());
        if (user!=null) {
            result.fail(MsgCodeUtil.MSG_CODE_SYSTEM_USER_NAME_EXIST);
        } else {
            user = new UserDetail();
            BeanCustomUtil.copyProperties(vo, user);
            user.setId(IdWorker.getId(user)+"");
            user.setNewRecord(true);
            user.setPassword(encoder.encode(vo.getPassword()));
//            user.setPassword(UserDetailService.entryptPassword(vo.getPassword()));
            if (0 < userService.save(user)) {
                result.success("user", user);
                log.info("注册用户: {}成功！", user.toString());
            } else{
                result.error(MsgCodeUtil.MSG_CODE_UNKNOWN);
                log.info("注册用户: {}失败！", user.toString());
            }
        }

        return (CommonResult) result.end();
    }


//    @ApiOperation(value = "登录")
//    @RequestMapping(value = "login", method = RequestMethod.POST)
    public CommonResult loginUser(@RequestBody @ApiParam("用户登录vo") UserBasicDTO vo) {
        CommonResult result = new CommonResult().init();
        UserDetail user = userService.getByLoginUsername(vo.getUsername());
//        if (user!=null && UserDetailService.validatePassword(vo.getPassword(), user.getPassword())) {
        if (user!=null && encoder.matches(vo.getPassword(), user.getPassword())) {
            result.success("user", user);

            String token = jwtUtil.createToken(user.getId(), user.getUsername());
            result.putItem("token", token);
        } else {
            result.fail(MsgCodeUtil.MSG_CODE_USERNAME_OR_PASSWORD_INCORRECT);
        }

        return (CommonResult) result.end();
    }

    @ApiOperation(value = "修改密码")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "userId",
                    value = "用户id",
                    paramType = "path",
                    dataType = "string",
                    required = true
            ),
    })
    @RequestMapping(value = "password/{userId}", method = RequestMethod.POST)
    public CommonResult updatePassword(@PathVariable String userId, @RequestBody @ApiParam("修改密码vo") UserPasswordDTO vo) {
        CommonResult result = new CommonResult().init();
        if (0 < userDetailService.updatePassword(userId, encoder.encode(vo.getNewPassword()))) {
            result.success();
        } else {
            result.fail(MsgCodeUtil.MSG_CODE_UNKNOWN);
        }

        return (CommonResult) result.end();
    }

    @ApiOperation(value = "完善个人信息")
    @ApiImplicitParams(
            @ApiImplicitParam(
                    name = "userId",
                    value = "用户id",
                    paramType = "path",
                    dataType = "string",
                    required = true
            )
    )
    @RequestMapping(value = "userInfo/{userId}", method = RequestMethod.POST)
    public CommonResult updateUserInfo(@Validated @RequestBody @ApiParam("用户信息vo") UserInfoDTO vo,
                                       @PathVariable String userId,
                                       BindingResult bindingResult) {
        CommonResult result = new CommonResult().init();
        // 参数验证
        if (bindingResult.hasErrors()) {
            return (CommonResult) result.failIllegalArgument(bindingResult.getFieldErrors()).end();
        }
        if (userService.get(userId)!=null) {
            UserInfo userInfo = userInfoService.get(userId);
            if (userInfo==null) {
                userInfo = new UserInfo();
                userInfo.setId(userId);
                userInfo.setNewRecord(true);
            }
            String timestamp = vo.getFoundDate() + "000";
            long longStamp = Long.parseLong(timestamp);
            userInfo.setFoundDate(new Date(longStamp));
            BeanCustomUtil.copyProperties(vo, userInfo);
            if (0 < userInfoService.save(userInfo)) {
                result.success("userInfo", userInfo);
                log.info("完善用户信息成功: {}", userInfo);
            } else {
                result.fail(MsgCodeUtil.MSG_CODE_UNKNOWN);
                log.info("完善用户信息失败: {}", userInfo);
            }

        } else {
            result.failCustom(MsgCodeUtil.MSG_CODE_DATA_NOT_EXIST, "不存在该用户");
            log.info("完善用户信息失败: 不存在该用户{}", userId);
        }


        return (CommonResult) result.end();
    }

    @ApiOperation(value = "获取用户详细信息")
    @ApiImplicitParams(
            @ApiImplicitParam(
                    name = "userId",
                    value = "用户id",
                    paramType = "path",
                    dataType = "string",
                    required = true
            )
    )
    @RequestMapping(value = "userInfo/{userId}", method = RequestMethod.GET)
    public CommonResult getUserDetailInfo(@PathVariable String userId) {
        CommonResult result = new CommonResult().init();
        if (!StringUtils.hasLength(userId)) {
            return (CommonResult) result.fail(MsgCodeUtil.MSG_CODE_ILLEGAL_ARGUMENT).end();
        }
        UserInfoVO vo = userInfoService.getUserInfoVO(userId);

        if (vo!=null) {
            result.success("userInfo", vo);
        } else {
            result.failCustom(MsgCodeUtil.MSG_CODE_DATA_NOT_EXIST, "不存在该用户");
        }

        return (CommonResult) result.end();
    }

}
