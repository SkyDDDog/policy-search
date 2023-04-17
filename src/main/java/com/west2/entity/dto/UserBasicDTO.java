package com.west2.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Accessors(chain = true)
@Data
public class UserBasicDTO {

    @ApiModelProperty(value = "用户名", example = "testUser")
    @NotBlank(message = "用户名不能为空")
    private String username;

    @ApiModelProperty(value = "密码" , example = "123456")
    @NotBlank(message = "用户密码不能为空")
    private String password;


}
