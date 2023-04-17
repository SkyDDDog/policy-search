package com.west2.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.west2.entity.UserDetail;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<UserDetail> {
}
