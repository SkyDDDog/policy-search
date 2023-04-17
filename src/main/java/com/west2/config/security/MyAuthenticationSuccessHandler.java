package com.west2.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.west2.common.CommonResult;
import com.west2.util.JwtTokenUtil;
import com.west2.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class MyAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        CommonResult result = new CommonResult().init();
        MyUserDetails principal = (MyUserDetails) authentication.getPrincipal();
        String accessToken = principal.getAccessToken();
        String refreshToken = principal.getRefreshToken();
        result.success("access_token", accessToken);
        result.putItem("refresh_token", refreshToken);
        result.putItem("user_id", principal.getId());
        //设置返回请求头
        response.setContentType("application/json;charset=utf-8");
        //写出流
        PrintWriter out = response.getWriter();
        ObjectMapper mapper = new ObjectMapper();
        out.write(mapper.writeValueAsString((CommonResult)result.end()));
        out.flush();
        out.close();
    }

}
