package com.west2.entity.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 政策信息视图层
 */
@Data
@Accessors(chain = true)
public class PolicyCollectVO extends PolicyVO {

    private Boolean collected;


}
