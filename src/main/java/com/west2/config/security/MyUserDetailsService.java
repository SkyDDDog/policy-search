package com.west2.config.security;

import com.west2.entity.Role;
import com.west2.entity.UserDetail;
import com.west2.entity.UserRole;
import com.west2.service.RoleService;
import com.west2.service.UserDetailService;
import com.west2.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserDetailService userService;
    @Autowired
    private RoleService roleService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetail bean = userService.getByLoginUsername(username);
        if (bean==null) {
            throw new UsernameNotFoundException("User "+username+" didn't exist.");
        }
        //获得角色信息
        List<Role> roles = roleService.getUserRolesByUserId(bean.getId());
        //格式转化
        List<GrantedAuthority> authority = roles.stream().map(i->new SimpleGrantedAuthority(i.getCode())).collect(Collectors.toList());
        MyUserDetails myUser = new MyUserDetails(bean.getUsername(), bean.getPassword(), authority);
        myUser.setId(bean.getId());
        return myUser;
    }
}
