package com.west2.entity.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class CityVO {

    private String cityId;
    private String city;
    private List<AreaVO> areas;

}
