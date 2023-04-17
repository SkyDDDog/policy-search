package com.west2.entity.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class DistrictVO {

    private String provinceId;
    private String province;
    private List<CityVO> citys;

}
