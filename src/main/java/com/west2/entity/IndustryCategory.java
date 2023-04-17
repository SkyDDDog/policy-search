package com.west2.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.west2.entity.base.DataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Data
@Accessors(chain = true)
@TableName("`industry_category`")
@ApiModel(value = "IndustryCategory", description = "产业类别信息")
public class IndustryCategory extends DataEntity<IndustryCategory> {

    @ApiModelProperty(value = "一级类目id", example = "01")
    @NotBlank(message = "一级类目id不能为空")
    private String classId;

    @ApiModelProperty(value = "产业类别", example = "农业")
    @NotBlank(message = "产业类别不能为空")
    private String industryCategory;

}
