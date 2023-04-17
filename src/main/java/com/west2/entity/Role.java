package com.west2.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.west2.entity.base.DataEntity;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@TableName("`role`")
@Accessors(chain = true)
public class Role extends DataEntity<Role> {

    private String code;
    private String desc;

}
