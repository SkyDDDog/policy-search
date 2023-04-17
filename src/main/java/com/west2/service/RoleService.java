package com.west2.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.west2.entity.Role;
import com.west2.entity.UserRole;
import com.west2.mapper.RoleMapper;
import com.west2.service.base.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleService extends CrudService<RoleMapper, Role> {

    @Autowired
    private UserRoleService userRoleService;

    public List<Role> getUserRolesByUserId(String userId) {

        List<UserRole> userRoles = userRoleService.getUserRolesByUserId(userId);
        ArrayList<Role> result = new ArrayList<>(userRoles.size());
        for (UserRole userRole : userRoles) {
            result.add(this.get(userRole.getRoleId()));
        }
        return result;
    }

}
