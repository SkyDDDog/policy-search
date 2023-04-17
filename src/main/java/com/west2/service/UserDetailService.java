package com.west2.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.west2.entity.UserDetail;
import com.west2.entity.base.BaseEntity;
import com.west2.mapper.UserMapper;
import com.west2.service.base.CrudService;
import com.west2.util.Digests;
import com.west2.util.EncodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailService extends CrudService<UserMapper, UserDetail> {


    public static boolean validatePassword(String plainPassword, String password) {
        String plain = EncodeUtil.unescapeHtml(plainPassword);
        byte[] salt = EncodeUtil.decodeHex(password.substring(0, 16));
        byte[] hashPassword = Digests.sha1(plain.getBytes(), salt, 1024);
        return password.equals(EncodeUtil.encodeHex(salt) + EncodeUtil.encodeHex(hashPassword));
    }

    public static String entryptPassword(String plainPassword) {
        String plain = EncodeUtil.unescapeHtml(plainPassword);
        byte[] salt = Digests.generateSalt(8);
        byte[] hashPassword = Digests.sha1(plain.getBytes(), salt, 1024);
        return EncodeUtil.encodeHex(salt) + EncodeUtil.encodeHex(hashPassword);
    }
    public UserDetail getByLoginUsername(String username) {
        QueryWrapper<UserDetail> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        List<UserDetail> list = this.findList(wrapper);
        if (list.size()==1) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public int getRegisterCount() {
        QueryWrapper<UserDetail> wrapper = new QueryWrapper<>();
        wrapper.eq("del_flag", BaseEntity.DEL_FLAG_NORMAL);
        return this.findList(wrapper).size();
    }

    public int updatePassword(String userId, String newPassword) {
        UserDetail userDetail = this.get(userId);
        userDetail.setPassword(newPassword);
        return this.save(userDetail);
    }

}
