package com.west2.entity;

import com.west2.entity.base.DataEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;


@Data
@Accessors(chain = true)
public class UserCollection extends DataEntity<UserCollection> {

    @ApiModelProperty(value = "用户id", example = "1631327384979914753")
    @NotBlank(message = "用户id不能为空")
    private String userId;

    @ApiModelProperty(value = "政策id", example = "1642800952519426049")
    @NotBlank(message = "政策id不能为空")
    private String policyId;

}
