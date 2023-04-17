package com.west2.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.west2.entity.base.DataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@Accessors(chain = true)
@TableName("`user_info`")
@ApiModel(value = "UserInfo", description = "用户信息")
public class UserInfo extends DataEntity<UserInfo> {

    @ApiModelProperty(value = "公司名称", example = "西二在线")
    @NotBlank(message = "公司名称不能为空")
    private String companyName;

    @ApiModelProperty(value = "产业类别", example = "教育")
    @NotBlank(message = "产业类别不能为空")
    private String industryCategory;

    @ApiModelProperty(value = "所在地", example = "福建福州")
    @NotBlank(message = "所在地不能为空")
    private String district;

    @ApiModelProperty(value = "注册资金(万元)", example = "100")
    private String fund;

    @ApiModelProperty(value = "成立时间", example = "")
    private Date foundDate;

}
