package com.west2.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.west2.entity.base.DataEntity;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@TableName("`user_role`")
@Accessors(chain = true)
public class UserRole extends DataEntity<UserRole> {

    private String userId;
    private String roleId;

}
