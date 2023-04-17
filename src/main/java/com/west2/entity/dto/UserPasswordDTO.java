package com.west2.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;


@Accessors(chain = true)
@Data
public class UserPasswordDTO {


    @ApiModelProperty(value = "新密码", example = "123456")
    @NotBlank(message = "新密码不能为空")
    private String newPassword;

}
