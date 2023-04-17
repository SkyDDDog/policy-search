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
@TableName("`industry_class`")
@ApiModel(value = "IndustryClass", description = "产业类别一级类目")
public class IndustryClass extends DataEntity<IndustryClass> {

    @ApiModelProperty(value = "产业类别一级类目", example = "传统产业")
    @NotBlank(message = "产业类别一级类目不能为空")
    private String industryClass;

}
