package com.west2.util;

import cn.hutool.core.util.StrUtil;
import com.west2.config.JwtProperties;
import com.west2.config.security.MyUserDetails;
import com.west2.entity.UserDetail;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenUtil {

    private final String CLAIM_KEY_USERID = "user_id";
    private final String CLAIM_KEY_USERNAME = "user_name";
    private final String CLAIM_KEY_CREATED = "create_date";
    private final String CLAIM_KEY_ROLES = "roles";

    @Autowired
    private JwtProperties jwtProperties;

    // 根据用户信息生成token
    public Map<String, String> generateToken(MyUserDetails userDetails) {
        Map<String, String> rst = new HashMap<String, String>();
        // 访问token
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERID, userDetails.getId());
        claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED, new Date());
        rst.put(getAccessTokenKey(), generateToken(claims, jwtProperties.getAccessExpiration()));

        rst.put(getRefreshTokenKey(), generateToken(claims, jwtProperties.getRefreshExpiration()));

//        claims.put(CLAIM_KEY_ROLES, userDetails.getAuthorities());
        rst.put(getRoleTokenKey(), generateToken(claims, jwtProperties.getRolesExpiration()));

        return rst;
    }

    // 根据权限生成JWT的token
    private String generateToken(Map<String, Object> claims, Integer seconds) {
        return Jwts.builder().setClaims(claims).setExpiration(generateExpirationDate(seconds))
                .signWith(SignatureAlgorithm.HS512, jwtProperties.getSecret()).compact();
    }

    // token中解出用户名
    public String getUserNameFromToken(String token) {
        String username;
        try {
            Claims claims = getClaimsFromToken(token);
            log.info("claims: {}", claims);
//            username = claims.getSubject();
            username = (String) claims.get(CLAIM_KEY_USERNAME);
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    public String getUserIdFromToken(String token) {
        String userId;
        try {
            Claims claims = getClaimsFromToken(token);
            log.info("claims: {}", claims);
            userId = claims.get(CLAIM_KEY_USERID).toString();
        } catch (Exception e) {
            userId = null;
        }
        return userId;
    }

    //token中解出claims
    private Claims getClaimsFromToken(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(jwtProperties.getSecret())
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
        }
        return claims;
    }

    public static String getIdFromContext() {
        MyUserDetails principal = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal!=null) {
            return principal.getId();
        }
        return null;
    }

    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + jwtProperties.getAccessExpiration() * 1000);
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        String username = getUserNameFromToken(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        Date expiredDate = getExpiredDateFromToken(token);
        return expiredDate.before(new Date());
    }

    public boolean isTokenSameUser(String tokenBody, String username) {
        String userNameFromToken = this.getUserNameFromToken(tokenBody);
        if (StrUtil.isEmpty(userNameFromToken)) {
            return false;
        }
        return userNameFromToken.equals(username);
    }

    private Date getExpiredDateFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        if (claims==null) {
            return new Date();
        }
        return claims.getExpiration();
    }

    /**
     * 生成token的过期时间
     */
    private Date generateExpirationDate(Integer seconds) {
        return new Date(System.currentTimeMillis() + (int) (seconds * 1000));
    }
    //根据token获得roles
    public List<GrantedAuthority> getRolesFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        List<HashMap> roles =  (List<HashMap>) claims.get(CLAIM_KEY_ROLES);
        List<GrantedAuthority> authority = roles.stream().map(i->new SimpleGrantedAuthority((String) i.get("authority"))).collect(Collectors.toList());
        return authority;
    }

    //几个key及生成方式
    public String getAccessTokenKey(){
        return "accesstoken";
    }
    public String getAccessTokenKey(String username){
        return username+":accesstoken";
    }
    public String getRefreshTokenKey(){
        return "refreshtoken";
    }
    public String getRefreshTokenKey(String username){
        return username+":refreshtoken";
    }
    public String getRoleTokenKey(){
        return "roletoken";
    }
    public String getRoleTokenKey(String username){
        return username+":roletoken";
    }

    public String refreshHeadToken(String refreshtoken,String accesstoken) {
        if (StrUtil.isEmpty(refreshtoken)) {
            return null;
        }
        String username = getUserNameFromToken(accesstoken);
        if (StrUtil.isEmpty(username)) {
            return null;
        }
        // 如果token在30分钟之内刚刷新过，返回原token
        if (accesstoken != null) {
            return "";
        } else {
            Map<String, Object> accessClaims = new HashMap<>();
            accessClaims.put(CLAIM_KEY_USERNAME,username);
            accessClaims.put(CLAIM_KEY_CREATED, new Date());
            return generateToken(accessClaims, jwtProperties.getAccessExpiration());
        }
    }



}
