package com.west2.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.west2.entity.Role;
import com.west2.entity.UserRole;
import com.west2.mapper.UserRoleMapper;
import com.west2.service.base.CrudService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRoleService extends CrudService<UserRoleMapper, UserRole> {

    public List<UserRole> getUserRolesByUserId(String userId) {
        QueryWrapper<UserRole> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        return this.findList(wrapper);
    }

}
