package com.west2.config.security;

import com.west2.config.JwtProperties;
import com.west2.util.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtProperties jwtProperties;
    @Resource
    private MyUserDetailsService myUserDetailsService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
//        log.info("进入token过滤器");
        // 取出auth
        String authHeader = request.getHeader(jwtProperties.getHeaderName());
//        log.info("取出Auth头: {}", authHeader);
        if (authHeader != null && authHeader.startsWith(jwtProperties.getTokenHead())) {
            // tokenBody = jwttoken
            String tokenBody = authHeader.substring(jwtProperties.getTokenHead().length());
//            log.info("取出token主体: {}", tokenBody);
            if (tokenBody != null) {
                // 没过期
                String username = jwtTokenUtil.getUserNameFromToken(tokenBody);
                boolean isTokenExpired = jwtTokenUtil.isTokenExpired(tokenBody);
                if (username != null && !isTokenExpired && SecurityContextHolder.getContext().getAuthentication() == null) {
                    // 根据用户名，读取权限明细
                    MyUserDetails userDetails = (MyUserDetails) myUserDetailsService.loadUserByUsername(username);
                    if (jwtTokenUtil.isTokenSameUser(tokenBody, userDetails.getUsername())) {
                        // 生成authentication，
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            }
        }
        filterChain.doFilter(request, response);

    }
}
