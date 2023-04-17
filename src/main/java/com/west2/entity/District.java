package com.west2.entity;

import com.west2.entity.base.DataEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Accessors(chain = true)
@Data
public class District extends DataEntity<District> {

    @ApiModelProperty(value = "父级挂接id", example = "0")
    @NotBlank(message = "父级挂接id不能为空")
    private String pid;

    @ApiModelProperty(value = "区划编码", example = "140000")
    @NotBlank(message = "区划编码不能为空")
    private String code;

    @ApiModelProperty(value = "区划名称", example = "山西省")
    @NotBlank(message = "区划名称不能为空")
    private String name;

    @ApiModelProperty(value = "级次id 0:省/自治区/直辖市 1:市级 2:县级", example = "0")
    @NotBlank(message = "级次id不能为空")
    private String level;

    public static final String PROVINCE_LEVEL = "0";
    public static final String CITY_LEVEL = "1";
    public static final String COUNTY_LEVEL = "1";

}
